package com.bhavesh.resume.service.impl;

import com.bhavesh.resume.dto.auth.AuthResponse;
import com.bhavesh.resume.dto.auth.LoginRequest;
import com.bhavesh.resume.dto.auth.RegisterRequest;
import com.bhavesh.resume.entity.User;
import com.bhavesh.resume.exception.DuplicateUserException;
import com.bhavesh.resume.exception.InvalidCredentialsException;
import com.bhavesh.resume.repository.UserRepository;
import com.bhavesh.resume.service.AuthService;
import com.bhavesh.resume.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService  jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException(
                    "Email is already registered"
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.RECRUITER)
                .build();

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}
