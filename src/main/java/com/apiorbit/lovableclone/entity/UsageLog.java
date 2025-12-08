package com.apiorbit.lovableclone.entity;

import java.time.Instant;

public class UsageLog {

    private Long id;
    private User userId;
    private Project projectId;
    private String action;
    private Integer tokenUsed;
    private Integer durationMs;
    private String jsonb;
    private Instant createdAt;
}
