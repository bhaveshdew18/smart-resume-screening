package com.bhavesh.resume.exception;

public record ErrorResponse(
        int status,
        String message
) {
}
