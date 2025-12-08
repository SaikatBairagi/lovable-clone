package com.apiorbit.lovableclone.dto.project;


import java.time.Instant;


public record ProjectSummeryResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
