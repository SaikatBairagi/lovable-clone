package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.MessageRole;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {

    Long id;
    ChatSession chatSession;
    String content;
    String toolCalls;
    Integer tokenUsed;
    MessageRole role;
    Instant createdAt;
}
