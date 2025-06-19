package br.com.tech.os.ostech.model.dto.orderServiceDTO;

import br.com.tech.os.ostech.exception.InvalidOrderServiceIdException;

public record OrderServiceCreateDTO(String smartphoneId, String clientId, String budgetId, String status) {

    public OrderServiceCreateDTO {
        if (smartphoneId == null || smartphoneId.isBlank()) {
            throw new InvalidOrderServiceIdException("Smartphone ID cannot be null or blank");
        }
        if (clientId == null || clientId.isBlank()) {
            throw new InvalidOrderServiceIdException("Client ID cannot be null or blank");
        }
    }

}
