package com.bhavesh.resume.service.impl;

import com.bhavesh.resume.dto.request.CreateCandidateRequest;
import com.bhavesh.resume.dto.response.CandidateResponse;
import com.bhavesh.resume.entity.Candidate;
import com.bhavesh.resume.exception.CandidateNotFoundException;
import com.bhavesh.resume.exception.DuplicateCandidateException;
import com.bhavesh.resume.parser.ResumeParser;
import com.bhavesh.resume.repository.CandidateRepository;
import com.bhavesh.resume.service.CandidateService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {


    private final CandidateRepository candidateRepository;
    private final ResumeParser resumeParser;

    public CandidateServiceImpl(CandidateRepository candidateRepository,  ResumeParser resumeParser) {
        this.candidateRepository = candidateRepository;
        this.resumeParser = resumeParser;
    }

    @Override
    public CandidateResponse createCandidate(CreateCandidateRequest request) {

        if(candidateRepository.existsByEmail(request.email())) {
            throw new DuplicateCandidateException(request.email());
        }

        Candidate candidate = Candidate.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .resumeText(request.resumeText())
                .build();

        Candidate savedCandidate = candidateRepository.save(candidate);

        return mapToResponse(savedCandidate);
    }

    @Override
    public List<CandidateResponse> getCandidates() {

        return candidateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CandidateResponse getCandidateById(long id) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new CandidateNotFoundException(id)
                );

        return mapToResponse(candidate);
    }

    @Override
    public CandidateResponse uploadResume(Long id, MultipartFile file) {

        Candidate candidate =  candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));

        try {

            String resumeText = resumeParser.extractText(file);

            candidate.setResumeText(resumeText);

            Candidate savedCandidate = candidateRepository.save(candidate);

            return mapToResponse(savedCandidate);

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Failed to process resume",
                    exception
            );
        }
    }


    private CandidateResponse mapToResponse(Candidate candidate) {

        return new CandidateResponse(
                candidate.getId(),
                candidate.getName(),
                candidate.getEmail(),
                candidate.getPhone(),
                candidate.getCreatedAt()
        );
    }

}
