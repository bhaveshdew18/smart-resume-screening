package com.bhavesh.resume.exception;

public class ResumeNotFoundException extends RuntimeException {

    public ResumeNotFoundException(Long candidateId) {

        super("Resume not found for candidate: " + candidateId);
    }
}
