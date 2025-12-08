package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.PreviewStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Internal;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Preview {

    Long id;
    Project project;
    String nameSpace;
    String podName;
    String previewUrl;
    PreviewStatus status;
    Instant createdAt;
    Instant terminatedAt;
    Instant startedAt;
}
