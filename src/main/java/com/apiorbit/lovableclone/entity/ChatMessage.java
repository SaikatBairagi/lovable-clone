package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.MessageRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
@Entity
@Table(name="chat_message")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumns({
                    @JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = false),
                    @JoinColumn(name="project_id", referencedColumnName = "project_id", nullable = false)
            })
    ChatSession chatSession;

    @Column(columnDefinition = "text", nullable = false)
    String content;

    String toolCalls;

    Integer tokenUsed = 0;
    @Enumerated(EnumType.STRING)
            @Column(nullable=false)
    MessageRole role;

    @CreationTimestamp
    Instant createdAt;
}
