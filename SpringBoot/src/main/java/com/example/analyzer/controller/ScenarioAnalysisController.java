package com.example.analyzer.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.analyzer.model.ScenarioAnalysisRequest;
import com.example.analyzer.model.ScenarioAnalysisResponse;
import com.example.analyzer.service.AiService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Allow React app to connect
public class ScenarioAnalysisController {
    
    @Autowired
    private AiService aiService;
    
    @PostMapping("/analyze-scenario")
    public ResponseEntity<ScenarioAnalysisResponse> analyzeScenario(
            @RequestBody ScenarioAnalysisRequest request) {
        
        ScenarioAnalysisResponse response = aiService.generateAnalysis(request);
        return ResponseEntity.ok(response);
    }
}