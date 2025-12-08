package com.apiorbit.lovableclone.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plan {

    Long id;
    String name;
    String stripePriceId;
    Integer maxProjects;
    Integer maxTokensPerDay;
    Integer maxPreview;
    Boolean unlimitedAi;
    String features;
    String price;
    Boolean active;
    Instant createdAt;
    Instant deletedAt;
}
