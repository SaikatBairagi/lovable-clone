package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.PreviewStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatSession {

    Project project;
    User user;
    String title;
    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;

}
