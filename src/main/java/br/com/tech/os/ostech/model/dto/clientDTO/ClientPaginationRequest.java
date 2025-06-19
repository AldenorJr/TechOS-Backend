package br.com.tech.os.ostech.model.dto.clientDTO;

import br.com.tech.os.ostech.enums.SearchType;
import br.com.tech.os.ostech.exception.InvalidExceptionClientPagination;

public record ClientPaginationRequest (
    Integer page,
    Integer size,
    SearchType searchType,
    String searchValue
) {
    public ClientPaginationRequest {
        if (page == null || page < 0) {
            throw new InvalidExceptionClientPagination("Page must be a non-negative integer");
        }
        if (size == null || size <= 0) {
            throw new InvalidExceptionClientPagination("Size must be a positive integer");
        }

    }
}
