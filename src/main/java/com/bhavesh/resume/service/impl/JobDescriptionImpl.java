package com.bhavesh.resume.service.impl;

import com.bhavesh.resume.dto.request.CreateJobDescriptionRequest;
import com.bhavesh.resume.dto.response.JobDescriptionResponse;
import com.bhavesh.resume.entity.JobDescription;
import com.bhavesh.resume.exception.JobDescriptionNotFoundException;
import com.bhavesh.resume.repository.JobDescriptionRepository;
import com.bhavesh.resume.service.JobDescriptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobDescriptionImpl implements JobDescriptionService {


    private final JobDescriptionRepository jobDescriptionRepository;

    public  JobDescriptionImpl(JobDescriptionRepository jobDescriptionRepository) {

        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    @Override
    public JobDescriptionResponse createJobDescription(CreateJobDescriptionRequest request) {

        JobDescription jobdescription = JobDescription.builder()
                .title(request.title())
                .description(request.description())
                .requiredSkills(request.requiredSkills())
                .build();

        JobDescription savedJobDescription =
                jobDescriptionRepository.save(jobdescription);

        return mapToResponse(savedJobDescription);
    }

    @Override
    public List<JobDescriptionResponse> getAllJobDescriptions() {

        return jobDescriptionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public JobDescriptionResponse getJobDescriptionById(Long id) {

        JobDescription jobDescription =
                jobDescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new JobDescriptionNotFoundException(id)
                        );

        return mapToResponse(jobDescription);
    }

    private JobDescriptionResponse mapToResponse(
            JobDescription jobDescription
    ) {

        return new JobDescriptionResponse(
                jobDescription.getId(),
                jobDescription.getTitle(),
                jobDescription.getDescription(),
                jobDescription.getRequiredSkills(),
                jobDescription.getCreatedAt()
        );
    }
}
