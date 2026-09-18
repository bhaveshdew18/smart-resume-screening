package com.bhavesh.resume.exception;

public class ScreeningResultNotFoundException
        extends RuntimeException {

  public ScreeningResultNotFoundException(Long id) {
    super("Screening result not found with id: " + id);
  }
}