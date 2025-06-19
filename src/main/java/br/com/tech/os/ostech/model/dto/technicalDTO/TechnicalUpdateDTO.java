package br.com.tech.os.ostech.model.dto.technicalDTO;

import br.com.tech.os.ostech.exception.InvalidTechnicalFieldException;

public record TechnicalUpdateDTO(String name) {

    public TechnicalUpdateDTO {
        if (name == null || name.isBlank()) {
            throw new InvalidTechnicalFieldException("Name cannot be null or blank");
        }
    }

}
