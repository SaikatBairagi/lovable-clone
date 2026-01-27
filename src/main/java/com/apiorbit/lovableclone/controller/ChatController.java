package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.chat.ChatRequest;
import com.apiorbit.lovableclone.service.GenAIService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    GenAIService genAIService;

    @PostMapping(value = "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody ChatRequest request){
        return genAIService.streamChat(request.projectId(), request.chatMessage())
                .map(data -> ServerSentEvent.<String>builder().data(data).build());

    }
}
