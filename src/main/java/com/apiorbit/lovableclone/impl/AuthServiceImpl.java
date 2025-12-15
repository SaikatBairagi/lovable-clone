package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.auth.AuthResponse;
import com.apiorbit.lovableclone.dto.auth.LoginRequest;
import com.apiorbit.lovableclone.dto.auth.SignUpRequest;
import com.apiorbit.lovableclone.dto.auth.UserProfileResponse;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.mapper.MemberResponseMapper;
import com.apiorbit.lovableclone.repository.UserRepository;
import com.apiorbit.lovableclone.security.JwtService;
import com.apiorbit.lovableclone.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    MemberResponseMapper memberResponseMapper;
    AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public AuthResponse signUp(SignUpRequest signUpRequest) throws RuntimeException {
        log.info("Inside signUp: {}", signUpRequest);
        String userEmail = signUpRequest.email().trim();
        userRepository.findByEmail(userEmail)
                .ifPresent(existingUser -> {
                    throw new RuntimeException("User with email " + userEmail + " already exists");
                });
        //Persisting User
        User user = User.builder()
                .email(userEmail)
                .lastName(signUpRequest.lastName())
                .firstName(signUpRequest.firstName())
                .passwordHash(passwordEncoder.encode(signUpRequest.password()))
                .build();
        user = userRepository.save(user);

        //get JWT token
        String accessToken = jwtService.generateToken(user);
        UserProfileResponse userProfileResponse = memberResponseMapper.toUserProfileResponse(user);

        return new AuthResponse(accessToken, userProfileResponse);
    }

    @Override
    public AuthResponse logIn(LoginRequest login) {
        log.info("Inside logIn: {}", login);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.email(),
                        login.password()
                )
        );
        User user = (User)authentication.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        UserProfileResponse userProfileResponse = memberResponseMapper.toUserProfileResponse(user);
        return new AuthResponse(accessToken, userProfileResponse);
    }

    @Override
    public UserProfileResponse viewProfile() {
        return null;
    }
}
