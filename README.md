# Scenario Analyzer

A full-stack web application that leverages AI to analyze business scenarios and provide structured recommendations based on user constraints.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [API Documentation](#api-documentation)
- [AI Implementation Details](#ai-implementation-details)
- [Error Handling](#error-handling)
- [Security Considerations](#security-considerations)

## Features

- Input form for describing scenarios and specifying constraints
- AI-powered analysis providing:
  - Scenario Summary
  - Potential Pitfalls
  - Proposed Strategies
  - Recommended Resources
  - Disclaimer
- Spring Boot backend with OpenAI GPT-4 integration
- React frontend with responsive design

## Architecture

```
/
├── SpringBoot/                 # Backend Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/analyzer/
│   │   │   │   ├── controller/    # REST controllers
│   │   │   │   ├── model/         # Data models
│   │   │   │   ├── service/       # Business logic & OpenAI integration
│   │   │   │   └── ScenarioAnalyzerApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties  # Configuration
│   │   └── test/                  # JUnit tests
│   ├── mvnw                       # Maven wrapper
│   └── pom.xml                    # Maven dependencies
└── react-frontend/               # React frontend
    ├── public/
    ├── src/
    │   ├── components/            # React components
    │   ├── App.js                 # Main application component
    │   └── App.css                # Styles
    └── package.json              # NPM dependencies
```

## Prerequisites
- Java 17 or higher
- Node.js 14 or higher
- npm 6 or higher
- OpenAI API key
- Modern web browser (Chrome, Firefox, Safari, Edge)
- Internet connection for API calls

## Installation & Setup

### Backend Setup

1. Configure the environment variables in `src/main/resources/application.properties`:

```properties
# Server configuration
server.port=8080

# OpenAI configuration
openai.api.key=${OPENAI_API_KEY}
openai.model=gpt-4o-mini

# CORS configuration
spring.mvc.cors.allowed-origins=http://localhost:3000
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE
spring.mvc.cors.allowed-headers=*

# Logging
logging.level.root=INFO
logging.level.com.example.analyzer=DEBUG
```

**Important**: Instead of hardcoding your API key, set it as an environment variable when running the application.

### Frontend Setup

The frontend communicates with the backend API at http://localhost:8080/api by default.

## Running the Application

### Backend

1. Navigate to the SpringBoot directory:
   ```bash
   cd SpringBoot
   ```

2. Build and run the Spring Boot application with your API key:
   ```bash
   OPENAI_API_KEY=your_api_key_here ./mvnw spring-boot:run
   ```

3. Verify the backend is running at http://localhost:8080

### Frontend

1. Navigate to the react-frontend directory:
   ```bash
   cd react-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. Access the application at http://localhost:3000

## Testing

The Spring Boot application includes comprehensive JUnit tests to ensure the reliability and functionality of the backend components.

### Controller Tests

`ScenarioAnalysisControllerTest.java` validates that:
- The API endpoint correctly accepts POST requests with scenario data
- Responses are formatted correctly with appropriate status codes
- Validation errors are handled properly for invalid requests

### Service Tests

`AiServiceTest.java` tests the core AI integration functionality:
- Mocks the OpenAI API calls to test without making actual external requests
- Ensures proper parsing of OpenAI responses into structured data
- Tests error handling for API failures

### Running Tests

To run the JUnit tests:
```bash
cd SpringBoot
./mvnw test
```

## API Documentation

### Endpoint: POST /api/analyze-scenario

Analyzes a scenario and returns structured recommendations.

#### Request Body:
```json
{
  "scenario": "Our team has a new client project with a tight deadline and limited budget.",
  "constraints": [
    "Budget: $10,000",
    "Deadline: 6 weeks",
    "Team of 3 developers"
  ]
}
```

#### Response Body:
```json
{
  "scenarioSummary": "A small team must deliver a client project within 6 weeks on a $10,000 budget.",
  "potentialPitfalls": [
    "Scope creep due to unclear requirements",
    "Underestimation of resource constraints",
    "Risk of burnout with limited manpower"
  ],
  "proposedStrategies": [
    "Define clear milestones and requirements early",
    "Implement lean project management principles",
    "Conduct weekly check-ins to monitor progress"
  ],
  "recommendedResources": [
    "Trello or Jira for agile task management",
    "Open-source libraries to reduce cost",
    "Online tutorials for rapid skill upskilling"
  ],
  "disclaimer": "These suggestions are generated by AI; consult subject matter experts for tailored guidance."
}
```

## AI Implementation Details

The application uses OpenAI's GPT-4o-mini model to analyze business scenarios. The service:

1. Takes an input of a detailed prompt with the user's scenario and constraints
2. Makes API calls to OpenAI with appropriate parameters:
   - Temperature: 0.3 (for more consistent outputs)
   - Max tokens: 1000 (for complete but concise responses)
   - Top_p: 0.95 (to reduce randomness further)
3. Parses the JSON response into a structured format for the frontend

The AI prompt has been carefully engineered to provide structured analysis with:
- Chain-of-thought reasoning process
- Strict output formatting requirements
- Verification steps to ensure valid JSON output

You can modify the prompt structure in `AiService.java` if you need to adjust the AI's analysis approach.

## Error Handling

The application includes several mechanisms to handle edge cases and errors:

### Backend Error Handling

1. **API Error Recovery**
   - The `AiService` catches and logs exceptions from OpenAI API calls
   - When the API fails, a fallback error response is generated instead of crashing
   - Error details are included in the response for debugging

2. **Input Validation**
   - Request data is validated before processing
   - Ensures scenario description is not empty
   - Verifies constraints are properly formatted

3. **JSON Parsing Safety**
   - Robust error handling for malformed JSON responses
   - Fallback to error response if OpenAI returns invalid data

4. **Rate Limiting Consideration**
   - The service architecture is designed to accommodate OpenAI rate limiting
   - Backoff strategies can be implemented if needed

### Frontend Error Handling

1. **Form Validation**
   - Input fields have validation to prevent empty submissions
   - Clear error messages guide users to provide proper input

2. **API Error Display**
   - Failed API calls show user-friendly error messages
   - Network connection issues are detected and reported

3. **Loading States**
   - Loading indicators display while waiting for AI analysis
   - Prevents multiple submissions during processing


