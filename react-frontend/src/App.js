// src/App.js
import React, { useState } from 'react';
import './App.css';
import ScenarioForm from './components/ScenarioForm';
import AnalysisResults from './components/AnalysisResults';

function App() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [analysisResults, setAnalysisResults] = useState(null);

  const handleSubmit = async (scenarioData) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await fetch('http://localhost:8080/api/analyze-scenario', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(scenarioData),
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      
      const data = await response.json();
      setAnalysisResults(data);
    } catch (err) {
      setError(`Error: ${err.message}`);
      console.error('Error submitting scenario:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Scenario Analysis Tool</h1>
        <p>Enter your scenario and constraints to get AI-powered analysis</p>
      </header>
      
      <main className="App-main">
        <ScenarioForm onSubmit={handleSubmit} disabled={loading} />
        
        {loading && <div className="loading">Analyzing your scenario...</div>}
        
        {error && (
          <div className="error-message">
            <h3>Error</h3>
            <p>{error}</p>
          </div>
        )}
        
        {analysisResults && !loading && !error && (
          <AnalysisResults results={analysisResults} />
        )}
      </main>
      
      <footer className="App-footer">
        <p>Scenario Analysis Tool &copy; 2025</p>
      </footer>
    </div>
  );
}

export default App;