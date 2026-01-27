package com.apiorbit.lovableclone.llm.advisor;

import com.apiorbit.lovableclone.dto.project.FileNode;
import com.apiorbit.lovableclone.impl.FileServiceImpl;
import com.apiorbit.lovableclone.repository.ProjectFileRepository;
import com.apiorbit.lovableclone.repository.ProjectRepository;
import com.apiorbit.lovableclone.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileTreeAdvisor implements StreamAdvisor {

    private final FileService fileService;
    @Override
    public Flux<ChatClientResponse> adviseStream(
            ChatClientRequest request,
            StreamAdvisorChain streamAdvisorChain) {
        Map<String, Object> context = request.context();
        Long projectId = Long.valueOf(context.getOrDefault("ProjectId",0).toString());
        ChatClientRequest augmentResponse = augmentRequestWithFileTree(request, projectId);
        return streamAdvisorChain.nextStream(augmentResponse);
    }

    private ChatClientRequest augmentRequestWithFileTree(
            ChatClientRequest request,
            Long projectId) {

        List<Message> messages = request.prompt().getInstructions();

        Message systemMessage = messages.stream()
                .filter(message -> message.getMessageType()== MessageType.SYSTEM)
                .findFirst()
                .orElse(null);

        List<Message> userMessage = messages.stream()
                .filter(message -> message.getMessageType() != MessageType.SYSTEM)
                .toList();

        List<Message> augmentMessage = new ArrayList<>();
        //get the file Tree
        List<FileNode> fileTree = fileService.getFileTree(projectId);

        // Add original system message
        if (systemMessage != null) {
            augmentMessage.add(systemMessage);
        }

        String fileTreeMessage = "\n ---- FILE TREE ---- \n"+fileTree.toString();

        augmentMessage.add(new SystemMessage(fileTreeMessage));

        augmentMessage.addAll(userMessage);

        return request
                .mutate()
                .prompt(new Prompt(augmentMessage, request.prompt().getOptions()))
                .build();

    }

    @Override
    public String getName() {
        return "FileTreeAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
