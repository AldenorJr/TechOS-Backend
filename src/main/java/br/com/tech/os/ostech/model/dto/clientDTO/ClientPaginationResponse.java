package br.com.tech.os.ostech.model.dto.clientDTO;

import br.com.tech.os.ostech.enums.SearchType;
import br.com.tech.os.ostech.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class ClientPaginationResponse {

    private Integer totalPages;
    private Integer currentPage;
    private List<Client> clientsPage;
    private Integer pageSize;

}
