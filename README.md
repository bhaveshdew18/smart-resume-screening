# Smart Resume Screening Tool

An AI-powered backend application that automates resume screening by analyzing candidate resumes against job descriptions and generating structured skill-match results.

Built with **Java 21, Spring Boot, Spring AI, Gemini, PostgreSQL, Spring Data JPA, JWT, and Docker-ready architecture**.

---

## Overview

Recruiters often need to manually review large numbers of resumes against job requirements. This project automates the initial screening process by extracting resume content, sending it to an AI-powered screening service, and generating structured candidate-job matching results.

The system allows recruiters to:

* Register and authenticate securely
* Create and manage candidate profiles
* Upload PDF resumes
* Extract text from resumes automatically
* Create job descriptions
* Define required skills
* Screen candidates against job descriptions using AI
* Generate match scores and skill analysis
* Store screening results for later review
* Access APIs using JWT-based authentication
* Restrict functionality using role-based authorization

---

## Key Features

### Candidate Management

* Create candidate profiles
* Store candidate contact information
* Upload PDF resumes
* Extract resume text automatically
* Retrieve candidate information

### Job Description Management

* Create job descriptions
* Store required skills
* Retrieve available job descriptions
* Associate candidates with specific jobs

### AI Resume Screening

The screening engine evaluates:

* Overall match score
* Matched skills
* Missing skills
* Experience match
* AI-generated candidate summary

The AI is instructed to use only information explicitly present in the resume and avoid inventing candidate qualifications.

### Authentication & Authorization

* User registration
* Secure BCrypt password hashing
* JWT-based authentication
* Stateless authentication
* Role-based authorization
* `ADMIN` and `RECRUITER` roles
* Protected REST APIs

### Resume Processing

PDF resumes are processed using Apache PDFBox:

```text
PDF Resume
    ↓
PDFBox
    ↓
Extracted Text
    ↓
Candidate Profile
    ↓
AI Screening
```

---

## Architecture

The application follows a layered backend architecture.

```text
                         Client
                           │
                           ▼
                    REST Controllers
                           │
                           ▼
                       Services
                           │
              ┌────────────┴────────────┐
              │                         │
              ▼                         ▼
        Repositories              AI Service
              │                         │
              ▼                         ▼
        PostgreSQL                  Gemini
```

### Package Structure

```text
src/main/java/com/bhavesh/resume
│
├── controller
│   ├── AuthController
│   ├── CandidateController
│   ├── JobDescriptionController
│   └── ScreeningController
│
├── service
│   ├── AuthService
│   ├── CandidateService
│   ├── JobDescriptionService
│   ├── ScreeningService
│   ├── JwtService
│   └── impl
│
├── repository
│   ├── UserRepository
│   ├── CandidateRepository
│   ├── JobDescriptionRepository
│   └── ScreeningResultRepository
│
├── entity
│   ├── User
│   ├── Candidate
│   ├── JobDescription
│   └── ScreeningResult
│
├── dto
│   ├── auth
│   ├── request
│   └── response
│
├── ai
│   ├── model
│   ├── prompt
│   └── service
│
├── parser
│   └── ResumeParser
│
├── exception
│
└── config
    ├── SecurityConfig
    └── AIConfig
```

---

## Technology Stack

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java 21           | Backend language               |
| Spring Boot 4.1.1 | Application framework          |
| Spring Web MVC    | REST APIs                      |
| Spring Data JPA   | Database persistence           |
| Hibernate         | ORM                            |
| PostgreSQL        | Relational database            |
| Spring Security   | Authentication & authorization |
| JWT               | Stateless authentication       |
| BCrypt            | Password hashing               |
| Spring AI         | AI integration                 |
| Google Gemini     | Resume analysis                |
| Apache PDFBox     | PDF text extraction            |
| Maven             | Build & dependency management  |
| Lombok            | Boilerplate reduction          |
| Docker            | Containerization               |

---

## AI Screening Flow

The core functionality works as follows:

```text
Candidate
    │
    │ Upload Resume
    ▼
PDF Resume
    │
    ▼
Apache PDFBox
    │
    ▼
Resume Text
    │
    ├───────────────┐
    │               │
    ▼               ▼
Candidate       Job Description
Resume           Requirements
    │               │
    └───────┬───────┘
            ▼
       Spring AI
            │
            ▼
         Gemini
            │
            ▼
    Structured Result
            │
     ┌──────┼─────────────┐
     ▼      ▼             ▼
 Match    Skills       Experience
 Score    Analysis       Match
            │
            ▼
        PostgreSQL
```

---

## AI Output

The AI screening service returns a structured response:

```json
{
  "matchScore": 85,
  "matchedSkills": [
    "Java",
    "Spring Boot",
    "PostgreSQL"
  ],
  "missingSkills": [
    "Kubernetes"
  ],
  "experienceMatch": true,
  "summary": "The candidate demonstrates strong alignment with the backend development requirements."
}
```

The AI prompt is designed to prevent unsupported assumptions:

* Only use information explicitly present in the resume
* Do not invent skills
* Do not invent experience
* Do not assume related technologies are equivalent
* Report missing required skills
* Keep the summary factual
* Return a score between 0 and 100

---

## Database Model

### User

```text
User
├── id
├── name
├── email
├── password
├── role
└── createdAt
```

### Candidate

```text
Candidate
├── id
├── name
├── email
├── phone
├── resumeText
└── createdAt
```

### Job Description

```text
JobDescription
├── id
├── title
├── description
├── requiredSkills
└── createdAt
```

### Screening Result

```text
ScreeningResult
├── id
├── candidate
├── jobDescription
├── matchScore
├── experienceMatch
├── matchedSkills
├── missingSkills
├── summary
└── createdAt
```

Relationships:

```text
Candidate
    │
    └──────< ScreeningResult >──────┐
                                    │
                              JobDescription
```

---

## Authentication Flow

```text
POST /api/auth/register
          │
          ▼
     Validate User
          │
          ▼
    BCrypt Password
          │
          ▼
      PostgreSQL


POST /api/auth/login
          │
          ▼
   Verify Credentials
          │
          ▼
      Generate JWT
          │
          ▼
       Client
          │
          ▼
Authorization: Bearer <JWT>
          │
          ▼
   Spring Security
          │
          ▼
      Protected API
```

---

## Roles

### RECRUITER

Recruiters can work with:

* Candidates
* Resumes
* Job descriptions
* Resume screening

### ADMIN

Administrative functionality is separated from recruiter functionality through Spring Security role-based authorization.

Authorization is implemented using:

```java
@PreAuthorize("hasRole('RECRUITER')")
```

---

## API Endpoints

### Authentication

#### Register

```http
POST /api/auth/register
```

Request:

```json
{
  "name": "Bhavesh",
  "email": "bhavesh@example.com",
  "password": "password123"
}
```

#### Login

```http
POST /api/auth/login
```

Request:

```json
{
  "email": "bhavesh@example.com",
  "password": "password123"
}
```

---

### Candidates

```http
POST   /api/candidates
GET    /api/candidates
GET    /api/candidates/{id}
POST   /api/candidates/{id}/resume
```

Resume upload uses:

```text
multipart/form-data
```

with the PDF supplied as the `file` parameter.

---

### Job Descriptions

```http
POST /api/jobs
GET  /api/jobs
GET  /api/jobs/{id}
```

---

### Screening

```http
POST /api/screenings
GET  /api/screenings/{id}
GET  /api/screenings/candidate/{candidateId}
GET  /api/screenings/job/{jobId}
```

---

## Configuration

Create PostgreSQL database:

```sql
CREATE DATABASE smart_resume_screening;
```

Configure environment variables.

### Windows PowerShell

```powershell
$env:DB_PASSWORD="your_postgres_password"
$env:GEMINI_API_KEY="your_gemini_api_key"
$env:JWT_SECRET="your_long_random_jwt_secret"
```

The application uses:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/smart_resume_screening}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

spring.ai.google.genai.api-key=${GEMINI_API_KEY}

jwt.secret=${JWT_SECRET}
jwt.expiration=3600000
```

### Important

Never commit API keys, passwords, or JWT secrets to Git.

Use environment variables or a secret-management solution.

---

## Running the Application

### Clone

```bash
git clone https://github.com/<your-username>/smart-resume-screening.git
cd smart-resume-screening
```

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

Application:

```text
http://localhost:8080
```

Health endpoint:

```text
http://localhost:8080/actuator/health
```

---

## Example Workflow

A typical recruiter workflow:

### 1. Register

```text
POST /api/auth/register
```

### 2. Login

```text
POST /api/auth/login
```

Receive JWT.

### 3. Create candidate

```text
POST /api/candidates
```

### 4. Upload resume

```text
POST /api/candidates/{id}/resume
```

### 5. Create job

```text
POST /api/jobs
```

### 6. Screen candidate

```text
POST /api/screenings
```

Example:

```json
{
  "candidateId": 1,
  "jobDescriptionId": 1
}
```

### 7. Retrieve result

```text
GET /api/screenings/{id}
```

---

## Error Handling

The application uses centralized exception handling through `@RestControllerAdvice`.

Examples include:

```text
CandidateNotFoundException
DuplicateCandidateException
JobDescriptionNotFoundException
ResumeNotFoundException
ScreeningResultNotFoundException
InvalidCredentialsException
DuplicateUserException
```

Example response:

```json
{
  "status": 404,
  "message": "Candidate not found"
}
```

---

## Security

Security features currently implemented:

* Stateless authentication
* JWT bearer authentication
* BCrypt password hashing
* Protected REST endpoints
* Role-based authorization
* Public authentication endpoints
* Environment-based secrets
* No plaintext password storage

---

## Development Status

### Completed

* [x] Spring Boot project setup
* [x] PostgreSQL integration
* [x] Candidate management
* [x] Job description management
* [x] PDF resume parsing
* [x] Spring AI integration
* [x] Gemini integration
* [x] AI resume screening
* [x] Screening result persistence
* [x] Global exception handling
* [x] User registration
* [x] BCrypt password hashing
* [x] JWT authentication
* [x] JWT validation
* [x] Role-based authorization

### Planned

* [ ] Pagination and filtering
* [ ] Improved AI prompt versioning
* [ ] AI model tracking
* [ ] Comprehensive unit tests
* [ ] Integration tests
* [ ] Docker Compose
* [ ] Production deployment
* [ ] CI/CD
* [ ] Improved API documentation
* [ ] Recruiter dashboard
* [ ] Candidate ranking/search
* [ ] Screening history and analytics

---

## Engineering Practices

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

AI functionality is isolated behind an AI service abstraction:

```text
ScreeningService
       ↓
ResumeScreeningAIService
       ↓
Gemini
```

This keeps AI-specific implementation details separated from the core business logic.

---

## Future Improvements

Potential improvements include:

### Resume Intelligence

* Resume section extraction
* Experience timeline extraction
* Education extraction
* Project extraction
* Skill normalization
* Duplicate resume detection

### Screening

* Configurable scoring weights
* Skill-category matching
* Experience-level matching
* Job-specific screening criteria
* Candidate ranking

### Platform

* Recruiter dashboard
* Screening analytics
* Search and filtering
* Pagination
* Screening history
* Export reports

### Infrastructure

* Docker Compose
* CI/CD pipeline
* Cloud deployment
* Centralized logging
* Monitoring
* Automated integration testing

---

## Project Goal

The goal of this project is to demonstrate practical backend engineering skills while integrating AI into a real-world recruitment workflow.

The project focuses on:

* REST API development
* Spring Boot architecture
* Database design
* Authentication and authorization
* AI integration
* PDF processing
* Exception handling
* Persistence
* Clean separation of responsibilities
* Production-oriented backend practices

---

## Author

**Bhavesh Dewangan**

Computer Science & Engineering Student
Java Backend Developer

### Technologies

```text
Java
Spring Boot
Spring Security
Spring AI
Gemini
PostgreSQL
JPA / Hibernate
JWT
Docker
REST APIs
```
