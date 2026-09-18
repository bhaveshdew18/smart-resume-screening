package com.bhavesh.resume.ai.model;

import java.util.List;

public record ScreeningAIResponse(

        Integer matchScore,

        List<String> matchedSkills,

        List<String> missingSkills,

        Boolean experienceMatch,

        String summary
) {

}
