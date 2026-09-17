package com.bhavesh.resume.exception;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(Long id) {

        super("Could not find candidate with id: " + id);
    }
}
