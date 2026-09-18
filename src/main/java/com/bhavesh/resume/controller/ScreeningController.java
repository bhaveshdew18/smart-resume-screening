package com.bhavesh.resume.controller;

import com.bhavesh.resume.dto.request.CreateScreeningRequest;
import com.bhavesh.resume.dto.response.ScreeningResultResponse;
import com.bhavesh.resume.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    private ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {

        this.screeningService = screeningService;
    }

    @GetMapping("/{id}")
    public ScreeningResultResponse getScreeningById(
            @PathVariable Long id
    ) {
        return screeningService.getScreeningById(id);
    }

    @GetMapping("/candidate/{candidateId}")
    public List<ScreeningResultResponse> getByCandidate(
            @PathVariable Long candidateId
    ) {
        return screeningService.getScreeningsByCandidate(candidateId);
    }

    @GetMapping("/job/{jobDescriptionId}")
    public List<ScreeningResultResponse> getByJob(
            @PathVariable Long jobDescriptionId
    ) {
        return screeningService.getScreeningsByJob(jobDescriptionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScreeningResultResponse createScreening(
            @Valid @RequestBody CreateScreeningRequest request
    ) {

        return screeningService.createScreening(request);
    }
}
