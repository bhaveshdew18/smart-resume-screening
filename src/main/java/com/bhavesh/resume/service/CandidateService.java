package com.bhavesh.resume.service;

import com.bhavesh.resume.dto.request.CreateCandidateRequest;
import com.bhavesh.resume.dto.response.CandidateResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateService {

    CandidateResponse createCandidate(CreateCandidateRequest request) throws IllegalAccessException;

    List<CandidateResponse> getCandidates();

    CandidateResponse getCandidateById(long Id);

    CandidateResponse uploadResume(Long id, MultipartFile file);
}
