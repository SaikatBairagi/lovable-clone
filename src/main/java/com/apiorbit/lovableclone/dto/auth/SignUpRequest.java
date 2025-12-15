package com.apiorbit.lovableclone.dto.auth;

public record SignUpRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
