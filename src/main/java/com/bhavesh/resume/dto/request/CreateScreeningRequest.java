package com.bhavesh.resume.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateScreeningRequest(

        @NotNull(message = "Candidate ID is required")
        Long candidateId,

        @NotNull(message = "Job description ID is required")
        Long jobDescriptionId
) {
}
