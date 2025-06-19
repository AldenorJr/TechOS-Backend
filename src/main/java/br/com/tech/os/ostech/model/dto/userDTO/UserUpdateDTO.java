package br.com.tech.os.ostech.model.dto.userDTO;

import br.com.tech.os.ostech.exception.InvalidUserFieldException;

public record UserUpdateDTO(
        String name,
        String email,
        String password
) {

    public UserUpdateDTO {
        if (name == null || name.isBlank()) {
            throw new InvalidUserFieldException("Name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidUserFieldException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidUserFieldException("Password cannot be null or blank");
        }
    }

}
