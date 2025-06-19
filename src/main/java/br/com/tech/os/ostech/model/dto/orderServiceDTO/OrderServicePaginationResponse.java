package br.com.tech.os.ostech.model.dto.orderServiceDTO;

import br.com.tech.os.ostech.enums.SearchType;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class OrderServicePaginationResponse {

    private Integer totalPages;
    private Integer currentPage;
    private List<OrderService> orderServices;
    private Integer pageSize;

}
