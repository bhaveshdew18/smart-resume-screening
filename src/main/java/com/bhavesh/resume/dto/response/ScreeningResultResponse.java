package com.bhavesh.resume.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ScreeningResultResponse(

        Long id,

        Long candidateId,

        Long jobDescriptionId,

        Integer matchScore,

        Boolean experienceMatch,

        List<String> matchedSkills,

        List<String> missingSkills,

        String summary,

        LocalDateTime createdAt
) {
}
