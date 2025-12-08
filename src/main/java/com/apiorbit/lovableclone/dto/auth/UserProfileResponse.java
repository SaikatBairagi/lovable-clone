package com.apiorbit.lovableclone.dto.auth;


public record UserProfileResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String avatar
) {
}
