package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.auth.AuthResponse;
import com.apiorbit.lovableclone.dto.auth.LoginRequest;
import com.apiorbit.lovableclone.dto.auth.SignUpRequest;
import com.apiorbit.lovableclone.dto.auth.UserProfileResponse;

public interface AuthService {
    AuthResponse signUp(SignUpRequest signUpRequest);

    AuthResponse logIn(LoginRequest login);

    UserProfileResponse viewProfile();
}
