package com.bhavesh.resume.service.impl;

import com.bhavesh.resume.ai.model.ScreeningAIResponse;
import com.bhavesh.resume.ai.service.ResumeScreeningAIService;
import com.bhavesh.resume.dto.request.CreateScreeningRequest;
import com.bhavesh.resume.dto.response.ScreeningResultResponse;
import com.bhavesh.resume.entity.Candidate;
import com.bhavesh.resume.entity.JobDescription;
import com.bhavesh.resume.entity.ScreeningResult;
import com.bhavesh.resume.exception.CandidateNotFoundException;
import com.bhavesh.resume.exception.JobDescriptionNotFoundException;
import com.bhavesh.resume.exception.ResumeNotFoundException;
import com.bhavesh.resume.repository.CandidateRepository;
import com.bhavesh.resume.repository.JobDescriptionRepository;
import com.bhavesh.resume.repository.ScreeningResultRepository;
import com.bhavesh.resume.service.ScreeningService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bhavesh.resume.exception.ScreeningResultNotFoundException;

import java.util.List;


@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final CandidateRepository candidateRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final ScreeningResultRepository screeningResultRepository;
    private final ResumeScreeningAIService aiService;

    public ScreeningServiceImpl(
            CandidateRepository candidateRepository,
            JobDescriptionRepository jobDescriptionRepository,
            ScreeningResultRepository screeningResultRepository,
            ResumeScreeningAIService aiService
    ) {
        this.candidateRepository = candidateRepository;
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.screeningResultRepository = screeningResultRepository;
        this.aiService = aiService;
    }

    @Override
    @Transactional
    public ScreeningResultResponse createScreening(
            CreateScreeningRequest request) {

        Candidate candidate = candidateRepository
                .findById(request.candidateId())
                .orElseThrow(() ->
                        new CandidateNotFoundException(
                                request.candidateId()
                        )
                );

        JobDescription jobDescription = jobDescriptionRepository
                .findById(request.jobDescriptionId())
                .orElseThrow(() ->
                        new JobDescriptionNotFoundException(
                                request.jobDescriptionId()
                        )
                );

        validdateResume(candidate);

        ScreeningAIResponse aiResponse =
                aiService.screenResume(
                        candidate.getResumeText(),
                        jobDescription.getDescription(),
                        jobDescription.getRequiredSkills()
                );

        ScreeningResult result = ScreeningResult.builder()
                .candidate(candidate)
                .jobDescription(jobDescription)
                .matchScore(aiResponse.matchScore())
                .experienceMatch(aiResponse.experienceMatch())
                .matchedSkills(String.join(
                        ",",
                        aiResponse.matchedSkills()
                ))
                .missingSkills(String.join(
                        ",",
                        aiResponse.missingSkills()
                ))
                .summary(aiResponse.summary())
                .build();

        ScreeningResult savedResult =
                screeningResultRepository.save(result);

        return mapToResponse(savedResult);
    }

    @Override
    @Transactional(readOnly = true)
    public ScreeningResultResponse getScreeningById(Long id) {

        ScreeningResult result = screeningResultRepository
                .findById(id)
                .orElseThrow(() ->
                        new ScreeningResultNotFoundException(id)
                );

        return mapToResponse(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreeningResultResponse> getScreeningsByCandidate(
            Long candidateId
    ) {

        if (!candidateRepository.existsById(candidateId)) {
            throw new CandidateNotFoundException(candidateId);
        }

        return screeningResultRepository
                .findByCandidateId(candidateId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreeningResultResponse> getScreeningsByJob(
            Long jobDescriptionId
    ) {

        if (!jobDescriptionRepository.existsById(jobDescriptionId)) {
            throw new JobDescriptionNotFoundException(jobDescriptionId);
        }

        return screeningResultRepository
                .findByJobDescriptionId(jobDescriptionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validdateResume(Candidate candidate) {

        if (candidate.getResumeText() == null ||
                candidate.getResumeText().isBlank()) {

            throw new ResumeNotFoundException(candidate.getId());
        }
    }

    private ScreeningResultResponse mapToResponse(
            ScreeningResult result
    ) {

        return new ScreeningResultResponse(
                result.getId(),
                result.getCandidate().getId(),
                result.getJobDescription().getId(),
                result.getMatchScore(),
                result.getExperienceMatch(),
                splitSkills(result.getMatchedSkills()),
                splitSkills(result.getMissingSkills()),
                result.getSummary(),
                result.getCreatedAt()
        );
    }

    private List<String> splitSkills(String skills) {

        if (skills == null || skills.isBlank()) {
            return List.of();
        }

        return List.of(skills.split(",\\s*"));
    }
}
