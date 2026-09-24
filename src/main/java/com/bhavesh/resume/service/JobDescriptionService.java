package com.bhavesh.resume.service;

import com.bhavesh.resume.dto.request.CreateJobDescriptionRequest;
import com.bhavesh.resume.dto.response.JobDescriptionResponse;

import java.util.List;

public interface JobDescriptionService {

    JobDescriptionResponse createJobDescription(CreateJobDescriptionRequest request);

    List<JobDescriptionResponse> getAllJobDescriptions();

    JobDescriptionResponse getJobDescriptionById(Long id);
}
