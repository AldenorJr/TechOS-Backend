package br.com.tech.os.ostech.model.dto.orderServiceDTO;

import br.com.tech.os.ostech.model.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OrderServiceLastUpdateResponse {

    private List<OrderService> lastUpdateOrders;

}
