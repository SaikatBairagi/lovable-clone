package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.auth.AuthResponse;
import com.apiorbit.lovableclone.dto.auth.UserProfileResponse;

public interface AuthService {
    AuthResponse signUp();

    AuthResponse logIn();

    UserProfileResponse viewProfile();
}
