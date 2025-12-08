package com.apiorbit.lovableclone.dto.auth;




public record SignUpRequest(
        String name,
        String email,
        String password
) {
}
