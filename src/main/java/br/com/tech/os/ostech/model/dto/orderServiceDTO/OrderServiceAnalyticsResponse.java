package br.com.tech.os.ostech.model.dto.orderServiceDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderServiceAnalyticsResponse {

    private Integer totalOrders;
    private Integer lastMonthOrders;
    private Integer lastWeekOrders;
    private Integer last24HoursOrders;
    private Integer exitOrders;
    private Integer exitOrdersLastMonth;
    private Integer exitOrdersLastWeek;
    private Integer exitOrdersLast24Hours;
    private Integer currentOrders;
    private Integer currentOrdersLastMonth;
    private Integer currentOrdersLastWeek;
    private Integer currentOrdersLast24Hours;

    private OrderServiceAnalyticsWeekResponse orderServiceAnalyticsWeekResponse;


}
