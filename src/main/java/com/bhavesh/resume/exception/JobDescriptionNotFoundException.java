package com.bhavesh.resume.exception;

public class JobDescriptionNotFoundException extends RuntimeException {
    public JobDescriptionNotFoundException(Long id) {
        super("Job description not found with id: " + id);
    }
}
