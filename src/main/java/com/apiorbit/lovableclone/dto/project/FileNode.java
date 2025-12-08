package com.apiorbit.lovableclone.dto.project;

import java.time.Instant;

public record FileNode(
        Long id,
        String path,
        Instant createdAt,
        Instant modifiedAt,
        Long updatedBy
) {
}
