// ScenarioAnalysisResponse.java
package com.example.analyzer.model;

import java.util.List;

public class ScenarioAnalysisResponse {
    private String scenarioSummary;
    private List<String> potentialPitfalls;
    private List<String> proposedStrategies;
    private List<String> recommendedResources;
    private String disclaimer;
    
    // Default constructor
    public ScenarioAnalysisResponse() {
    }
    
    // Getters and Setters
    public String getScenarioSummary() {
        return scenarioSummary;
    }
    
    public void setScenarioSummary(String scenarioSummary) {
        this.scenarioSummary = scenarioSummary;
    }
    
    public List<String> getPotentialPitfalls() {
        return potentialPitfalls;
    }
    
    public void setPotentialPitfalls(List<String> potentialPitfalls) {
        this.potentialPitfalls = potentialPitfalls;
    }
    
    public List<String> getProposedStrategies() {
        return proposedStrategies;
    }
    
    public void setProposedStrategies(List<String> proposedStrategies) {
        this.proposedStrategies = proposedStrategies;
    }
    
    public List<String> getRecommendedResources() {
        return recommendedResources;
    }
    
    public void setRecommendedResources(List<String> recommendedResources) {
        this.recommendedResources = recommendedResources;
    }
    
    public String getDisclaimer() {
        return disclaimer;
    }
    
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}

