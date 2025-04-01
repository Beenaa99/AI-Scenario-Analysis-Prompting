// AiServiceTest.java
package com.example.analyzer.service;

import com.example.analyzer.model.ScenarioAnalysisRequest;
import com.example.analyzer.model.ScenarioAnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiServiceTest {

    private static final String TEST_API_KEY = "test-api-key";
    private static final String TEST_MODEL = "gpt-4o-mini";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AiService aiService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(aiService, "apiKey", TEST_API_KEY);
        ReflectionTestUtils.setField(aiService, "model", TEST_MODEL);
        
        ReflectionTestUtils.setField(aiService, "restTemplate", restTemplate);
    }

    @Test
    public void testGenerateAnalysis() {
        // Arrange
        ScenarioAnalysisRequest request = new ScenarioAnalysisRequest();
        request.setScenario("test scenario");
        request.setConstraints(Arrays.asList("test constraint"));
        
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> choiceMessage = new HashMap<>();
        choiceMessage.put("content", "{\"scenarioSummary\":\"Test summary\",\"potentialPitfalls\":[\"Pitfall 1\"],\"proposedStrategies\":[\"Strategy 1\"],\"recommendedResources\":[\"Resource 1\"],\"disclaimer\":\"Test disclaimer\"}");
        Map<String, Object> choice = new HashMap<>();
        choice.put("message", choiceMessage);
        List<Map<String, Object>> choices = Arrays.asList(choice);
        mockResponse.put("choices", choices);
        
        when(restTemplate.postForObject(
            eq("https://api.openai.com/v1/chat/completions"),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(mockResponse);

        // Act
        ScenarioAnalysisResponse result = aiService.generateAnalysis(request);

        // Assert
        verify(restTemplate).postForObject(
            eq("https://api.openai.com/v1/chat/completions"),
            any(HttpEntity.class),
            eq(Map.class)
        );

        assertNotNull(result);
        assertEquals("Test summary", result.getScenarioSummary());
        assertEquals(1, result.getPotentialPitfalls().size());
        assertEquals("Pitfall 1", result.getPotentialPitfalls().get(0));
    }

    @Test
    public void testErrorHandling() {
        // Arrange
        ScenarioAnalysisRequest request = new ScenarioAnalysisRequest();
        request.setScenario("test scenario");
        request.setConstraints(Arrays.asList("test constraint"));
        
        when(restTemplate.postForObject(
            eq("https://api.openai.com/v1/chat/completions"),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        // Act
        ScenarioAnalysisResponse response = aiService.generateAnalysis(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getScenarioSummary().startsWith("Error:"));
        assertEquals(Arrays.asList("Error occurred"), response.getPotentialPitfalls());
        assertEquals(Arrays.asList("Please try again"), response.getProposedStrategies());
        assertEquals(Arrays.asList("Contact support"), response.getRecommendedResources());
    }
}