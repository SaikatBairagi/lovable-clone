package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.chat.ChatRequest;
import com.apiorbit.lovableclone.dto.chat.ChatResponse;
import com.apiorbit.lovableclone.service.ChatService;
import com.apiorbit.lovableclone.service.GenAIService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    GenAIService genAIService;
    ChatService chatService;

    @PostMapping(value = "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody ChatRequest request){
        return genAIService.streamChat(request.projectId(), request.chatMessage())
                .map(data -> ServerSentEvent.<String>builder().data(data).build());

    }

    @GetMapping("/api/chat/{projectId}")
    public ResponseEntity<List<ChatResponse>> getChatHistory(@PathVariable Long projectId){
        return ResponseEntity.ok().body(chatService.getProjectChatHistory(projectId));

    }
}
