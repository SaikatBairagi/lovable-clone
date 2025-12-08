package com.apiorbit.lovableclone.dto.project;

import com.apiorbit.lovableclone.dto.auth.UserProfileResponse;

import java.time.Instant;


public record ProjectResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        UserProfileResponse userProfile
) {
}
