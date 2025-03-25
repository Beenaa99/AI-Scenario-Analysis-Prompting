// src/components/ScenarioForm.js
import React, { useState } from 'react';

function ScenarioForm({ onSubmit, disabled }) {
  const [scenario, setScenario] = useState('');
  const [constraints, setConstraints] = useState(['']);
  const [errors, setErrors] = useState({ scenario: '', constraints: '' });

  const validateForm = () => {
    const newErrors = { scenario: '', constraints: '' };
    let isValid = true;
    
    if (!scenario.trim()) {
      newErrors.scenario = 'Scenario is required';
      isValid = false;
    } else if (scenario.trim().length < 10) {
      newErrors.scenario = 'Scenario must be at least 10 characters';
      isValid = false;
    }
    
    if (constraints.length === 0 || constraints.every(c => !c.trim())) {
      newErrors.constraints = 'At least one constraint is required';
      isValid = false;
    }
    
    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    // Filter out empty constraints
    const filteredConstraints = constraints.filter(c => c.trim());
    
    onSubmit({
      scenario,
      constraints: filteredConstraints
    });
  };

  const addConstraint = () => {
    setConstraints([...constraints, '']);
  };

  const removeConstraint = (index) => {
    if (constraints.length > 1) {
      const updatedConstraints = [...constraints];
      updatedConstraints.splice(index, 1);
      setConstraints(updatedConstraints);
    }
  };

  const updateConstraint = (index, value) => {
    const updatedConstraints = [...constraints];
    updatedConstraints[index] = value;
    setConstraints(updatedConstraints);
  };

  return (
    <div className="scenario-form-container">
      <form onSubmit={handleSubmit} className="scenario-form">
        <div className="form-group">
          <label htmlFor="scenario">Scenario:</label>
          <textarea
            id="scenario"
            value={scenario}
            onChange={(e) => setScenario(e.target.value)}
            placeholder="Describe your situation or challenge..."
            rows={5}
            disabled={disabled}
            className={errors.scenario ? 'error' : ''}
          />
          {errors.scenario && <div className="error-text">{errors.scenario}</div>}
        </div>

        <div className="form-group">
          <label>Key Constraints:</label>
          {errors.constraints && <div className="error-text">{errors.constraints}</div>}
          
          {constraints.map((constraint, index) => (
            <div key={index} className="constraint-input">
              <input
                type="text"
                value={constraint}
                onChange={(e) => updateConstraint(index, e.target.value)}
                placeholder="e.g., Budget: $10,000"
                disabled={disabled}
              />
              <button
                type="button"
                onClick={() => removeConstraint(index)}
                disabled={disabled || constraints.length <= 1}
                className="remove-btn"
              >
                Remove
              </button>
            </div>
          ))}
          
          <button
            type="button"
            onClick={addConstraint}
            disabled={disabled}
            className="add-constraint-btn"
          >
            Add Constraint
          </button>
        </div>

        <button
          type="submit"
          disabled={disabled}
          className="submit-btn"
        >
          {disabled ? 'Analyzing...' : 'Analyze Scenario'}
        </button>
      </form>
    </div>
  );
}

export default ScenarioForm;