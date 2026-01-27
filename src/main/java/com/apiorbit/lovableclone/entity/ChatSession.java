package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.PreviewStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
@Entity
@Table(name = "chat_session")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatSession {

    @EmbeddedId
    ChatMessageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    Instant updatedAt;

    Instant deletedAt;

}
