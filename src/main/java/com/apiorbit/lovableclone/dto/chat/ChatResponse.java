package com.apiorbit.lovableclone.dto.chat;

import com.apiorbit.lovableclone.entity.ChatEvent;
import com.apiorbit.lovableclone.entity.ChatSession;
import com.apiorbit.lovableclone.enumaration.MessageRole;


import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        ChatSession chatSession,
        String content,
        String toolCalls,
        Integer tokenUsed,
        MessageRole role,
        List<ChatEventResponse> chatEvents,
        Instant createdAt
) {
}
