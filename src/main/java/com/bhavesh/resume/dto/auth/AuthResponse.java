package com.bhavesh.resume.dto.auth;

public record AuthResponse(

        Long id,
        String name,
        String email,
        String role,
        String token
) {
}
