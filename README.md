# AI-Scenario-Analysis-Prompting
# Scenario Analyzer

A full-stack web application that leverages AI to analyze business scenarios and provide structured recommendations based on user constraints.

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

## Project Structure

## Prerequisites

- Java 17 or higher
- Node.js 14 or higher
- npm 6 or higher
- OpenAI API key

## Environment Variables

### Backend

The `application.properties` file in the `src/main/resources` directory should have the following content:

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



## Running the Application
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

The frontend communicates with the backend API at http://localhost:8080/api by default. 
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

## JUnit Tests

The Spring Boot application includes comprehensive JUnit tests to ensure the reliability and functionality of the backend components.

### Controller Tests

`ScenarioAnalysisControllerTest.java` validates that:
- The API endpoint correctly accepts POST requests with scenario data
- The controller properly delegates to the service layer
- Responses are formatted correctly with appropriate status codes
- Validation errors are handled properly for invalid requests

### Service Tests

`AiServiceTest.java` tests the core AI integration functionality:
- Mocks the OpenAI API calls to test without making actual external requests
- Verifies that requests to OpenAI are properly formatted with the correct:
  - Headers (Authorization, Content-Type)
  - Request body (model selection, temperature settings, prompt construction)
- Ensures proper parsing of OpenAI responses into structured data
- Tests error handling for API failures (401 unauthorized, network issues, etc.)

These tests use Mockito to replace external dependencies like the OpenAI API with controlled test doubles, allowing for consistent and reliable testing without requiring actual API keys or network connectivity.

### Running Tests

To run the JUnit tests:
```bash
cd SpringBoot
./mvnw test
```

This will execute all test classes and report any failures.
## API Documentation

### Endpoint: POST /api/analyze-scenario

Analyzes a scenario and returns structured recommendations.

Request Body:


Response Body:


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

## Edge Case Handling

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

### Security Considerations

1. **API Key Protection**
   - API keys are not hardcoded but provided as environment variables
   - Backend acts as a proxy to protect API keys from frontend exposure

2. **Input Sanitization**
   - User input is sanitized before processing
   - Prevents injection attacks and ensures prompt safety




