package com.bhavesh.resume.dto.response;

import java.time.LocalDateTime;

public record CandidateResponse(
        Long id,
        String name,
        String email,
        String phone,
        LocalDateTime createdAt
) {
}
