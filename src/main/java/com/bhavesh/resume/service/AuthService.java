package com.bhavesh.resume.service;

import com.bhavesh.resume.dto.auth.AuthResponse;
import com.bhavesh.resume.dto.auth.LoginRequest;
import com.bhavesh.resume.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
