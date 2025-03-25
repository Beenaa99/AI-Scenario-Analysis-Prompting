package com.example.analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import com.example.analyzer.model.ScenarioAnalysisRequest;
import com.example.analyzer.model.ScenarioAnalysisResponse;

@Service
public class AiService {
    
    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${openai.model}")
    private String model;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public ScenarioAnalysisResponse generateAnalysis(ScenarioAnalysisRequest request) {
        try {
            String prompt = buildPrompt(request);
            String apiUrl = "https://api.openai.com/v1/chat/completions";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                "role", "system", 
                "content", "You are an expert business analyst specializing in structured scenario analysis. You excel at identifying potential issues, proposing practical strategies, and recommending appropriate resources for various business scenarios."
            ));
            messages.add(Map.of("role", "user", "content", prompt));
            requestBody.put("messages", messages);
            
            // Lower temperature for more consistent outputs
            requestBody.put("temperature", 0.3);
            // Set token limit to ensure we get complete but not excessive responses
            requestBody.put("max_tokens", 1000);
            // Reduce randomness further
            requestBody.put("top_p", 0.95);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            Map response = restTemplate.postForObject(apiUrl, entity, Map.class);
            String content = extractContentFromResponse(response);
            
            return parseApiResponse(content);
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("Error generating analysis: " + e.getMessage());
        }
    }
    
    private String extractContentFromResponse(Map response) {
        List<Map> choices = (List<Map>) response.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, String> message = (Map<String, String>) firstChoice.get("message");
        return message.get("content");
    }
    
    private ScenarioAnalysisResponse parseApiResponse(String apiResponse) {
        try {
            return objectMapper.readValue(apiResponse, ScenarioAnalysisResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("Failed to parse API response: " + e.getMessage());
        }
    }
    
    private String buildPrompt(ScenarioAnalysisRequest request) {
        return """
            You are an AI assistant helping me analyze a scenario. I need you to generate a structured analysis in valid JSON format.
            
            Please follow this chain-of-thought process:
            
            Step 1: Carefully read and understand the scenario and constraints.
            Step 2: Identify the core problem or challenge presented in the scenario.
            Step 3: Consider how the constraints affect possible solutions.
            Step 4: Brainstorm potential pitfalls that might occur given the scenario and constraints.
            Step 5: Develop specific, actionable strategies that address both the scenario and potential pitfalls.
            Step 6: Identify resources that would be most helpful for implementing the strategies.
            Step 7: Create a brief, one-sentence disclaimer about limitations.
            Step 8: Format all information into a valid JSON response with the exact structure shown below.
            
            The final output must be valid JSON with the following structure and keys:
            {
              "scenarioSummary": "...",
              "potentialPitfalls": [...],
              "proposedStrategies": [...],
              "recommendedResources": [...],
              "disclaimer": "..."
            }
            
            Format requirements:
            - scenarioSummary: MUST be exactly 1-2 sentences summarizing the scenario
            - potentialPitfalls: MUST contain exactly 3-5 items, each as a single concise point
            - proposedStrategies: MUST contain exactly 3-5 items, each as a specific actionable recommendation
            - recommendedResources: MUST contain exactly 3-5 items, each a concrete tool, framework, or reference
            - disclaimer: MUST be exactly 1 sentence about limitations or expert consultation
            
            Here is an example of the input and expected output format:
            
            Example Input:
            Scenario: Our team has a new client project with a tight deadline and limited budget.
            Constraints: Budget: $10,000, Deadline: 6 weeks, Team of 3 developers
            
            Example Output:
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
            
            Now, analyze the following scenario and constraints:
            
            Scenario:
            %s
            
            Constraints:
            %s
            
            IMPORTANT VERIFICATION STEPS:
            1. Verify that your response contains ONLY valid JSON with no additional text, markdown, or explanations
            2. Verify that all keys match exactly: scenarioSummary, potentialPitfalls, proposedStrategies, recommendedResources, disclaimer
            3. Verify that all array fields contain 3-5 items, no more and no less
            4. Verify that the scenarioSummary is 1-2 sentences only
            5. Verify that the disclaimer is exactly 1 sentence
            
            Return only the JSON object with no additional text.
            """.formatted(
                request.getScenario(),
                String.join(", ", request.getConstraints())
            );
    }
    
    private ScenarioAnalysisResponse createErrorResponse(String errorMessage) {
        ScenarioAnalysisResponse response = new ScenarioAnalysisResponse();
        response.setScenarioSummary("Error: " + errorMessage);
        response.setPotentialPitfalls(List.of("Error occurred"));
        response.setProposedStrategies(List.of("Please try again"));
        response.setRecommendedResources(List.of("Contact support"));
        response.setDisclaimer("This error response was generated due to a system issue.");
        return response;
    }
}