package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.config.AiConfig;
import com.apiorbit.lovableclone.llm.PromptUtil;
import com.apiorbit.lovableclone.llm.advisor.FileTreeAdvisor;
import com.apiorbit.lovableclone.llm.tools.ReadProjectFiles;
import com.apiorbit.lovableclone.repository.ProjectFileRepository;
import com.apiorbit.lovableclone.repository.ProjectRepository;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.FileService;
import com.apiorbit.lovableclone.service.GenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
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



    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamChat(
            Long projectId,
            String userMessage) {
        Long userId = authUtil.getUserId();

        StringBuilder fullResponse = new StringBuilder();

        createChatSessionIfNotExist(userId, projectId);
        ReadProjectFiles readProjectFiles = new ReadProjectFiles(fileService, projectId);

        //Create a advisorParam to give LLM some context
        Map<String, Object>  advisorParam = Map.of(
                "UserId",userId,
                "ProjectId",projectId
        );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //calling LLM
        return chatClient
                .prompt()
                .system(PromptUtil.CODE_GENERATION_SYSTEM_PROMPT)
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
                    fullResponse.append(content);
                })
                .doOnComplete(() -> {
                    Schedulers.boundedElastic().schedule(() -> {
                        saveFileAndFileTreeOnCompletion(fullResponse.toString(), projectId);
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



    private void createChatSessionIfNotExist(
            Long userId,
            Long projectId) {
    }
}
