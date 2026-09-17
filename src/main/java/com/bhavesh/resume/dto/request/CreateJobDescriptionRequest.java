package com.bhavesh.resume.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateJobDescriptionRequest(

        @NotBlank(message = "Job title is required")
        String title,

        @NotBlank(message = "Job description is required")
        String description,

        String requiredSkills
) {
}
