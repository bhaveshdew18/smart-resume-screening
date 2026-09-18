package com.bhavesh.resume.ai.service.impl;

import com.bhavesh.resume.ai.model.ScreeningAIResponse;
import com.bhavesh.resume.ai.service.ResumeScreeningAIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ResumeScreeningAIServiceImpl implements ResumeScreeningAIService {

    private final ChatClient chatClient;

    public ResumeScreeningAIServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public ScreeningAIResponse screenResume(
            String resumeText,
            String jobDescription,
            String requiredSkills
    ) {

        return chatClient
                .prompt()
                .system("""
                    You are a technical resume screening assistant.

                    Your task is to evaluate a candidate's resume
                    against a job description.

                    Rules:
                    1. Use only information explicitly present in the resume.
                    2. Do not invent skills, experience, education, or projects.
                    3. Do not assume that related technologies are equivalent.
                    4. matchedSkills must contain skills explicitly supported
                       by the candidate's resume.
                    5. missingSkills must contain required skills that are
                       not explicitly supported by the resume.
                    6. matchScore must be an integer between 0 and 100.
                    7. Keep the summary concise and factual.
                    """)
                .user("""
                    Evaluate the following candidate.

                    JOB DESCRIPTION:
                    %s

                    REQUIRED SKILLS:
                    %s

                    CANDIDATE RESUME:
                    %s
                    """.formatted(
                        jobDescription,
                        requiredSkills,
                        resumeText
                ))
                .call()
                .entity(ScreeningAIResponse.class);
    }
}
