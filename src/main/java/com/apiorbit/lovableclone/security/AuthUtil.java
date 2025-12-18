package com.apiorbit.lovableclone.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthUtil {

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof  AuthenticatedUser))
            throw new AuthenticationCredentialsNotFoundException("User not found");
        return (Long)((AuthenticatedUser) authentication.getPrincipal()).id();
    }
}
