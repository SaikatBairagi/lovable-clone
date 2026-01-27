package com.apiorbit.lovableclone.service;

import reactor.core.publisher.Flux;

public interface GenAIService {
    Flux<String> streamChat(
            Long projectId,
            String chatMessage);
}
