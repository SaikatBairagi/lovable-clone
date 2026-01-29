package com.apiorbit.lovableclone.impl;


import com.apiorbit.lovableclone.entity.*;
import com.apiorbit.lovableclone.enumaration.ChatEventEnum;
import com.apiorbit.lovableclone.enumaration.MessageRole;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.llm.PromptUtil2;
import com.apiorbit.lovableclone.llm.advisor.FileTreeAdvisor;
import com.apiorbit.lovableclone.llm.parser.LLMResponseParser;
import com.apiorbit.lovableclone.llm.tools.ReadProjectFiles;
import com.apiorbit.lovableclone.repository.*;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.FileService;
import com.apiorbit.lovableclone.service.GenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenAIServiceImpl implements GenAIService {

    private final ChatClient chatClient;
    private final ProjectRepository projectRepository;
    private final CompletionService completionService;
    private final AuthUtil authUtil;
    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);
    private final FileService fileService;
    private final FileTreeAdvisor  fileTreeAdvisor;
    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;
    private final LLMResponseParser  responseParser;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventRepository  chatEventRepository;


    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamChat(
            Long projectId,
            String userMessage) {
        Long userId = authUtil.getUserId();

        StringBuilder fullResponse = new StringBuilder();

        ChatSession chatSession = createChatSessionIfNotExist(userId, projectId);
        ReadProjectFiles readProjectFiles = new ReadProjectFiles(fileService, projectId);

        //Create a advisorParam to give LLM some context
        Map<String, Object>  advisorParam = Map.of(
                "UserId",userId,
                "ProjectId",projectId
        );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        AtomicReference<Long> startTime = new AtomicReference<>(System.currentTimeMillis());
        AtomicReference<Long> endTime = new AtomicReference<>(0L);

        //calling LLM
        return chatClient
                .prompt()
                .system(PromptUtil2.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(readProjectFiles)
                .advisors(a -> {
                    a.params(advisorParam);
                    a.advisors(fileTreeAdvisor);
                })
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    String content = response.getResult().getOutput().getText();
                    if(content != null && !content.isEmpty() && endTime.get() == 0) { // first non-empty chunk received
                        endTime.set(System.currentTimeMillis());
                    }
                    fullResponse.append(content);
                })
                .doOnComplete(() -> {
                    Schedulers.boundedElastic().schedule(() -> {
                        long duration = (endTime.get() - startTime.get()) /  1000;
                        //saveFileAndFileTreeOnCompletion(fullResponse.toString(), projectId);
                        saveFileAndMessageEvents(fullResponse.toString(), chatSession, userMessage, duration);
                    });
                })
                .doOnError(error -> log.error("Error during streaming for projectId: {}", projectId))
                .handle((resp, sink) -> {
                    var result = resp != null ? resp.getResult() : null;
                    var output = result != null ? result.getOutput() : null;
                    var text   = output != null ? output.getText() : null;

                    if (text != null && !text.isEmpty()) {
                        sink.next(text);
                    }
                    // else: ignore non-text events
                });

    }

    private void saveFileAndFileTreeOnCompletion(String fullResponseFromLLM, Long projectId) {
//        String dummy = """
//                <message> I'm going to read the files and generate the code </message>
//                <file path="src/App.jsx">
//                    import App from './App.jsx'
//                    .....
//                </file>
//                <message> I'm going to read the files and generate the code </message>
//                <file path="src/App.jsx">
//                    import App from './App.jsx'
//                    .....
//                </file>
//                """;
        Matcher matcher = FILE_TAG_PATTERN.matcher(fullResponseFromLLM);
        log.info("--------Response from saveFileAndFileTreeOnCompletion-------- {} \n\n",fullResponseFromLLM);
        while (matcher.find()) {
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2).trim();

            fileService.saveFile(projectId, filePath, fileContent);
        }
    }

    private void saveFileAndMessageEvents(String fullResponseFromLLM, ChatSession chatSession, String userMessage, Long duration) {
        Long projectId = chatSession.getProject().getId();
        //Saving the user message
        ChatMessage chatMessage = ChatMessage.builder()
                .chatSession(chatSession)
                .role(MessageRole.USER)
                .content(userMessage)
                .build();

        chatMessageRepository.save(chatMessage);

        ChatMessage assistantChatMessage = ChatMessage.builder()
                .chatSession(chatSession)
                .role(MessageRole.ASSISTANT)
                .content("Assistant message here...")
                .build();
        assistantChatMessage = chatMessageRepository.saveAndFlush(assistantChatMessage);

        List<ChatEvent> chatEvents = responseParser.parseChatEvents(fullResponseFromLLM, assistantChatMessage);

        chatEvents
                .stream()
                        .filter(event -> event.getType() == ChatEventEnum.FILE_EDIT)
                                .forEach(file -> fileService.saveFile(projectId, file.getFilePath(), file.getContent()));
        chatEvents.add(0, (ChatEvent.builder()
                .type(ChatEventEnum.THOUGHT)
                .chatMessage(assistantChatMessage)
                .content("Thought for "+duration+"s")
                .sequence(0)
                .build()));
        log.info("chatEvents printed: \n {}", chatEvents);
        assistantChatMessage.setChatEvents(chatEvents);
        //chatEventRepository.saveAll(chatEvents);
        chatMessageRepository.save(assistantChatMessage);




    }


    /**
     * Creating a ChatSession for the chat to save
     * @param userId
     * @param projectId
     * @return
     */
    private ChatSession createChatSessionIfNotExist(
            Long userId,
            Long projectId) {
        ChatMessageId id = new ChatMessageId(userId, projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NoResourceFoundException("Project", projectId.toString()));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("User", userId.toString()));
        ChatSession chatSession = chatSessionRepository.findById(id).orElse(ChatSession.builder()
                .id(id)
                .project(project)
                .user(user)
                .build());

        return chatSessionRepository.save(chatSession);


    }
}
