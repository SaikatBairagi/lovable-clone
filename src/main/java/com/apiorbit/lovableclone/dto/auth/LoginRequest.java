package com.apiorbit.lovableclone.dto.auth;




public record LoginRequest(
        String email,
        String password
) {
}
