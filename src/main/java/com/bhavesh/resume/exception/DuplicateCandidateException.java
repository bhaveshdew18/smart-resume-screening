package com.bhavesh.resume.exception;

public class DuplicateCandidateException extends RuntimeException {
    public DuplicateCandidateException(String email) {

        super("The candidate with email " + email + " already exists.");
    }
}
