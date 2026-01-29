package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.chat.ChatResponse;
import com.apiorbit.lovableclone.entity.ChatMessage;
import com.apiorbit.lovableclone.entity.ChatMessageId;
import com.apiorbit.lovableclone.entity.ChatSession;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.mapper.ChatMapper;
import com.apiorbit.lovableclone.repository.ChatMessageRepository;
import com.apiorbit.lovableclone.repository.ChatSessionRepository;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AuthUtil authUtil;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMapper chatMapper;


    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getUserId();
        ChatSession chatSession = chatSessionRepository
                .findById(new ChatMessageId(projectId, userId))
                .orElseThrow(()-> new NoResourceFoundException("ChatSession", projectId.toString()));

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatSession(chatSession);
        log.info("ChatMessages found: {}", chatMessages);
        return chatMapper.fromListOfChatMessages(chatMessages);
    }
}
