package br.com.tech.os.ostech.model.dto.contactDTO;

import br.com.tech.os.ostech.exception.InvalidContactInformationException;

public record ContactUpdateDTO(String email, String phone) {

    public ContactUpdateDTO {
        if ((email == null || email.isBlank()) && (phone == null || phone.isBlank())) {
            throw new InvalidContactInformationException("Email and phone cannot be both null or blank");
        }
    }

}