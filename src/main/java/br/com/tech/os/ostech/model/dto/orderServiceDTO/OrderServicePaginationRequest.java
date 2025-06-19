package br.com.tech.os.ostech.model.dto.orderServiceDTO;

import br.com.tech.os.ostech.enums.OrderServiceSearchType;
import br.com.tech.os.ostech.exception.InvalidExceptionClientPagination;

public record OrderServicePaginationRequest (
    Integer page,
    Integer size,
    OrderServiceSearchType searchType,
    String searchValue
) {
    public OrderServicePaginationRequest {
        if (page == null || page < 0) {
            throw new InvalidExceptionClientPagination("Page must be a non-negative integer");
        }
        if (size == null || size <= 0) {
            throw new InvalidExceptionClientPagination("Size must be a positive integer");
        }

    }
}
