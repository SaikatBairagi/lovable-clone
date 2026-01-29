package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.ChatEventEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name="chat_event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chatMessage_id")
    private ChatMessage chatMessage;

    @Column(nullable = false)
    private Integer sequence;

    @Column(columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    private ChatEventEnum type;

    private String filePath;

    @Column(columnDefinition = "text")
    private String metadata;

    @CreationTimestamp
    private Instant createdAt;

    @Override
    public String toString() {
        return "ChatEvent{" +
                "chatMessage=" + chatMessage.getId() +
                ", sequence=" + sequence +
                ", type=" + type +
                ", filePath='" + filePath + '\'' +
                ", metadata='" + metadata + '\'' +
                '}';
    }
}
