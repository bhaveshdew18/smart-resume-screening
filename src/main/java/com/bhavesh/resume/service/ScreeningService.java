package com.bhavesh.resume.service;

import com.bhavesh.resume.dto.request.CreateScreeningRequest;
import com.bhavesh.resume.dto.response.ScreeningResultResponse;

import java.util.List;

public interface ScreeningService {

    ScreeningResultResponse createScreening(
            CreateScreeningRequest request
    );

    ScreeningResultResponse getScreeningById(Long id);

    List<ScreeningResultResponse> getScreeningsByCandidate(
            Long candidateId
    );

    List<ScreeningResultResponse> getScreeningsByJob(
            Long jobDescriptionId
    );
}
