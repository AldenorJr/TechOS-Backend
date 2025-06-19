package br.com.tech.os.ostech.model.dto.technicalDTO;

import br.com.tech.os.ostech.exception.InvalidTechnicalFieldException;

public record TechnicalCreateDTO(String name) {

    public TechnicalCreateDTO {
        if (name == null || name.isBlank()) {
            throw new InvalidTechnicalFieldException("Name cannot be null or blank");
        }
    }

}
