package com.apiorbit.lovableclone.dto.chat;

import com.apiorbit.lovableclone.entity.ChatMessage;
import com.apiorbit.lovableclone.enumaration.ChatEventEnum;

import java.time.Instant;

public record ChatEventResponse(
        Long id,
        ChatMessage chatMessage,
        Integer sequence,
        String content,
        ChatEventEnum type,
        String filePath,
        String metadata,
        Instant createdAt
) {
}
