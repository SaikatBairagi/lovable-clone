package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getProjectChatHistory(Long projectId);
}
