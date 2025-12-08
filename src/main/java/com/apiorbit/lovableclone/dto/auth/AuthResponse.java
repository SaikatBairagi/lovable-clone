package com.apiorbit.lovableclone.dto.auth;



public record AuthResponse(
        String token,
        UserProfileResponse userProfileResponse
) {
}
