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
            messages.add(Map.of("role", "system", "content", "You are a helpful AI assistant."));
            messages.add(Map.of("role", "user", "content", prompt));
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            
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
            You are an AI assistant helping me analyze a scenario. Please output valid JSON with the following structure and keys:
            {
              "scenarioSummary": "...",
              "potentialPitfalls": [...],
              "proposedStrategies": [...],
              "recommendedResources": [...],
              "disclaimer": "..."
            }
            
            Instructions:
            1. Scenario Summary: 1-2 sentences summarizing the user's scenario.
            2. Potential Pitfalls: a list (3-5 bullet points) of possible issues/risks.
            3. Proposed Strategies: a list (3-5 bullet points) of recommended solutions.
            4. Recommended Resources: a list (3-5 items) of tools, frameworks, or references.
            5. Disclaimer: a single sentence about AI limitations or the need for expert consultation.
            
            Scenario:
            [%s]
            
            Constraints:
            [%s]
            
            Please do not include any additional keys in the output. Ensure the JSON is valid and can be parsed by a standard JSON parser.
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