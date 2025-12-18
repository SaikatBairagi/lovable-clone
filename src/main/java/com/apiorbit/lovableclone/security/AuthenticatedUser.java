package com.apiorbit.lovableclone.security;

public record AuthenticatedUser(
        Long id,
        String email
) {
}
