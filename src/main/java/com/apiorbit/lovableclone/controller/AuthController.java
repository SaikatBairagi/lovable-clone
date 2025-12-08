package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.auth.AuthResponse;
import com.apiorbit.lovableclone.dto.auth.LoginRequest;
import com.apiorbit.lovableclone.dto.auth.SignUpRequest;
import com.apiorbit.lovableclone.dto.auth.UserProfileResponse;
import com.apiorbit.lovableclone.service.AuthService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("NullableProblems")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok().body(authService.signUp());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest login){
        return ResponseEntity.ok().body(authService.logIn());
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId = 1L;
        return ResponseEntity.ok().body(authService.viewProfile());
    }

}
