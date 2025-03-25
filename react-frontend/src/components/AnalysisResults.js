// src/components/AnalysisResults.js
import React from 'react';

function AnalysisResults({ results }) {
  const {
    scenarioSummary,
    potentialPitfalls,
    proposedStrategies,
    recommendedResources,
    disclaimer
  } = results;

  return (
    <div className="analysis-results">
      <h2>Analysis Results</h2>
      
      <div className="result-section">
        <h3>Scenario Summary</h3>
        <p>{scenarioSummary}</p>
      </div>
      
      <div className="result-section">
        <h3>Potential Pitfalls</h3>
        <ul>
          {potentialPitfalls.map((pitfall, index) => (
            <li key={index}>{pitfall}</li>
          ))}
        </ul>
      </div>
      
      <div className="result-section">
        <h3>Proposed Strategies</h3>
        <ul>
          {proposedStrategies.map((strategy, index) => (
            <li key={index}>{strategy}</li>
          ))}
        </ul>
      </div>
      
      <div className="result-section">
        <h3>Recommended Resources</h3>
        <ul>
          {recommendedResources.map((resource, index) => (
            <li key={index}>{resource}</li>
          ))}
        </ul>
      </div>
      
      <div className="result-section disclaimer">
        <strong>Disclaimer:</strong> {disclaimer}
      </div>
    </div>
  );
}

export default AnalysisResults;