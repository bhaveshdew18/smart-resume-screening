package com.bhavesh.resume.ai.service;


import com.bhavesh.resume.ai.model.ScreeningAIResponse;

public interface ResumeScreeningAIService {

    ScreeningAIResponse screenResume(
            String resumeText,
            String jobDescription,
            String requiredSkills
    );
}
