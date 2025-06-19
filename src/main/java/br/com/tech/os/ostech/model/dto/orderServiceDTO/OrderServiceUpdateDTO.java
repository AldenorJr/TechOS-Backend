package br.com.tech.os.ostech.model.dto.orderServiceDTO;

import java.util.Date;

public record OrderServiceUpdateDTO(
    String smartphoneId,
    String budgetId,
    String status,      
    String clientId,
    Date departureDate
    ) {

    public OrderServiceUpdateDTO {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Client ID cannot be null or blank");
        }
        if (smartphoneId == null || smartphoneId.isBlank()) {
            throw new IllegalArgumentException("Smartphone ID cannot be null or blank");
        }
    }

}
