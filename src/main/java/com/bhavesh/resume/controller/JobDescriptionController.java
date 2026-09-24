package com.bhavesh.resume.controller;

import com.bhavesh.resume.dto.request.CreateJobDescriptionRequest;
import com.bhavesh.resume.dto.response.JobDescriptionResponse;
import com.bhavesh.resume.entity.JobDescription;
import com.bhavesh.resume.service.JobDescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;
    public JobDescriptionController(JobDescriptionService jobDescriptionService) {
        this.jobDescriptionService = jobDescriptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobDescriptionResponse createJobDescription(
            @Valid @RequestBody CreateJobDescriptionRequest request
    ) {
       return  jobDescriptionService.createJobDescription(request);
    }

    @GetMapping
    public List<JobDescriptionResponse> getAllJobDescriptions() {
        return jobDescriptionService.getAllJobDescriptions();
    }

    @GetMapping("/{id}")
    public JobDescriptionResponse getJobDescriptionById(
            @PathVariable Long id
    ) {
        return jobDescriptionService.getJobDescriptionById(id);
    }
}
