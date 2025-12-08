package com.apiorbit.lovableclone.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectFile {

    Long id;
    Project project;
    String path;
    String minioObjectKey;
    User  createdBy;
    User  updatedBy;
    Instant createdAt;
    Instant updatedAt;
}
