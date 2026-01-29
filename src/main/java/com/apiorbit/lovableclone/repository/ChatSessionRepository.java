package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.ChatMessageId;
import com.apiorbit.lovableclone.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatMessageId> {
}
