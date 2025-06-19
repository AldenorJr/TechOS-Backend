package br.com.tech.os.ostech.model.dto.clientDTO;

import br.com.tech.os.ostech.exception.InvalidClientFieldException;

public record ClientCreateDTO(String name, String cpf) {

    public ClientCreateDTO {
        if (name == null || name.isBlank()) {
            throw new InvalidClientFieldException("Name cannot be null or blank");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidClientFieldException("CPF cannot be null or blank");
        }
    }

}
