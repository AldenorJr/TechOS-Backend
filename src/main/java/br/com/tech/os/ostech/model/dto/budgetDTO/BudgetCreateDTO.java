package br.com.tech.os.ostech.model.dto.budgetDTO;

public record BudgetCreateDTO(Double value, String description, String observation, Boolean approved, String technicalId) {
    
    public BudgetCreateDTO {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Value must be a positive number.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        if (observation == null) {
            observation = "";
        }
        if (technicalId == null || technicalId.isBlank()) {
            throw new IllegalArgumentException("Technical ID cannot be empty.");
        }
        if (approved == null) {
            approved = false;
        }
    }

}
