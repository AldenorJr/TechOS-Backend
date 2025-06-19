package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.model.dto.clientDTO.ClientAnalyticsResponse;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.tech.os.ostech.model.OrderService;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.tech.os.ostech.enums.OrderServiceSearchType;
import br.com.tech.os.ostech.service.OrderServiceService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/order-service")
public class OrderServiceController {

    private final OrderServiceService orderServiceService;

    @PostMapping()
    public ResponseEntity<OrderService> createOrderService(@RequestBody OrderServiceCreateDTO orderServiceCreateDTO) {
        OrderService orderService = orderServiceService.createOrderService(orderServiceCreateDTO);
        return ResponseEntity.status(201).body(orderService);
    }

    @GetMapping("/{orderServiceId}")
    public ResponseEntity<OrderService> getOrderServiceById(@PathVariable String orderServiceId) {
        OrderService orderService = orderServiceService.getOrderServiceById(orderServiceId);
        return ResponseEntity.ok(orderService);
    }

    @PutMapping("/{orderServiceId}")
    public ResponseEntity<OrderService> updateOrderService(@PathVariable String orderServiceId, @RequestBody OrderServiceUpdateDTO orderServiceUpdateDTO) {
        OrderService orderService = orderServiceService.updateOrderService(orderServiceId, orderServiceUpdateDTO);
        return ResponseEntity.ok(orderService);
    }

    @DeleteMapping("/{orderServiceId}")
    public ResponseEntity<Void> deleteOrderService(@PathVariable String orderServiceId) {
        orderServiceService.deleteOrderService(orderServiceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics")
    public ResponseEntity<OrderServiceAnalyticsResponse> getOrderAnalytics() {
        OrderServiceAnalyticsResponse analytics = orderServiceService.getOrderAnalytics();
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/recent")
    public ResponseEntity<OrderServiceLastUpdateResponse> getRecentOrders() {
        OrderServiceLastUpdateResponse recentOrders = orderServiceService.getRecentOrders();
        return ResponseEntity.ok(recentOrders);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<OrderService>> getAllOrderServices() {
        Iterable<OrderService> orderServices = orderServiceService.getAllOrderServices();
        return ResponseEntity.ok(orderServices);
    }

    
    @GetMapping("/pagination")
    public ResponseEntity<OrderServicePaginationResponse> getOrderServicesPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "searchType", defaultValue = "BLANK") OrderServiceSearchType searchType,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue) {
        OrderServicePaginationRequest orderServicePaginationRequest = new OrderServicePaginationRequest(page, size, searchType, searchValue);
        OrderServicePaginationResponse paginationResponse = orderServiceService.getOrderServicesPage(orderServicePaginationRequest);
        return ResponseEntity.ok(paginationResponse);
    }  

}
