package com.bhavesh.resume.controller;

import com.bhavesh.resume.dto.request.CreateCandidateRequest;
import com.bhavesh.resume.dto.response.CandidateResponse;
import com.bhavesh.resume.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private CandidateService candidateService;

    public  CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponse createCandidate(
            @Valid @RequestBody CreateCandidateRequest request) throws IllegalAccessException {
        return candidateService.createCandidate(request);
    }

    @GetMapping
    public List<CandidateResponse> getAllCandidates() {
        return candidateService.getCandidates();
    }

    @GetMapping("/{id}")
    public  CandidateResponse getCandidateById(@PathVariable long id) {
        return candidateService.getCandidateById(id);
    }

    @PostMapping(
            value = "/{id}/resume",
            consumes = "multipart/form-data"
    )
    public CandidateResponse uploadResume(
            @PathVariable long id,
            @RequestParam("file") MultipartFile file) {
        return candidateService.uploadResume(id, file);
    }
}
