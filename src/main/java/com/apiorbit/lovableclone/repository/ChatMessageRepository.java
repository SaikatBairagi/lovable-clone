package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.ChatMessage;
import com.apiorbit.lovableclone.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("""
            SELECT cm FROM ChatMessage cm
                        LEFT JOIN FETCH cm.chatEvents ce
                                    WHERE cm.chatSession =:chatSession
                                                ORDER BY cm.createdAt ASC, ce.sequence ASC
            """)
    List<ChatMessage> findByChatSession(ChatSession chatSession);
}
