package br.com.tech.os.ostech.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.tech.os.ostech.model.dto.clientDTO.ClientAnalyticsResponse;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import br.com.tech.os.ostech.exception.InvalidOrderServiceIdException;
import br.com.tech.os.ostech.model.Budget;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.OrderService;
import br.com.tech.os.ostech.model.Smartphone;
import br.com.tech.os.ostech.model.Status;
import br.com.tech.os.ostech.repository.OrderServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceService {

    private final OrderServiceRepository orderServiceRepository;
    private final SmartphoneService smartphoneService;
    private final ClientService clientService;
    private final BudgetService budgetService;

    @Transactional
    public OrderService createOrderService(OrderServiceCreateDTO orderServiceCreateDTO) {

        log.info("Creating Order Service");

        Client client = clientService.getClientById(orderServiceCreateDTO.clientId());
        Smartphone smartphone = smartphoneService.getSmartphoneById(orderServiceCreateDTO.smartphoneId());
        Budget budget = orderServiceCreateDTO.budgetId() == null
                ? null
                : budgetService.getBudgetById(orderServiceCreateDTO.budgetId());

        log.info("Smartphone: {}, Budget: {}, Client: {}", smartphone, budget, client);

        OrderService orderService = new OrderService();

        orderService.setSmartphoneId(smartphone);
        orderService.setBudgetId(budget);
        orderService.setClientId(client);
        orderService.setDepartureDate(null);
        orderService.setStatus(orderServiceCreateDTO.status() == null
                ? Status.OPEN
                : getStatusOrThrow(orderServiceCreateDTO.status()));
        orderService.setCreatedAt(new Date());
        orderService.setUpdatedAt(new Date());

        log.info("Order Service created with ID: {}", orderService.getId());

        return orderServiceRepository.save(orderService);
    }

    @Transactional
    public OrderService updateOrderService(String orderServiceId, OrderServiceUpdateDTO orderServiceUpdateDTO) {

        log.info("Updating order service with ID: {}", orderServiceId);

        OrderService orderService = orderServiceRepository.findById(orderServiceId)
                .orElseThrow(() -> new InvalidOrderServiceIdException("Order Service not found"));

        Client client = clientService.getClientById(orderServiceUpdateDTO.clientId());
        Smartphone smartphone = smartphoneService.getSmartphoneById(orderServiceUpdateDTO.smartphoneId());
        Budget budget = orderServiceUpdateDTO.budgetId() == null || orderServiceUpdateDTO.budgetId().isEmpty()
                ? null
                : budgetService.getBudgetById(orderServiceUpdateDTO.budgetId());

        log.info("Smartphone: {}, Budget: {}, Client: {}", smartphone, budget, client);

        orderService.setSmartphoneId(smartphone);
        if(budget != null) {
            orderService.setBudgetId(budget);
        }
        orderService.setClientId(client);
        orderService.setStatus(orderServiceUpdateDTO.status() == null
                ? orderService.getStatus()
                : getStatusOrThrow(orderServiceUpdateDTO.status()));
        orderService.setUpdatedAt(new Date());

        if (orderServiceUpdateDTO.departureDate() != null) {
            orderService.setDepartureDate(orderServiceUpdateDTO.departureDate());
        }

        log.info("Updated order service with ID: {} to smartphone: {}, budget: {}, client: {}", orderServiceId,
                smartphone, budget, client);

        return orderServiceRepository.save(orderService);
    }

    @Transactional
    public void deleteOrderService(String orderServiceId) {
        log.info("Deleting order service with ID: {}", orderServiceId);
        OrderService orderService = orderServiceRepository.findById(orderServiceId)
                .orElseThrow(() -> new InvalidOrderServiceIdException("Order service not found"));
        orderServiceRepository.delete(orderService);
        log.info("Deleted order service with ID: {}", orderServiceId);
    }

    public OrderServiceLastUpdateResponse getRecentOrders() {
        log.info("Fetching recent orders");
        List<OrderService> recentOrders = orderServiceRepository.findTop5ByOrderByUpdatedAtDesc();
        log.info("Found {} recent orders", recentOrders.size());
        return new OrderServiceLastUpdateResponse(recentOrders);
    }

    public OrderServiceAnalyticsResponse getOrderAnalytics() {
        log.info("Buscando analytics de clientes");
        int orderServices = Math.toIntExact(orderServiceRepository.count());
        log.info("Total de orders: {}", orderServices);
        int orderExitServices = orderServiceRepository.countByDepartureDateIsNotNull();

        LocalDate today = LocalDate.now();
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate lastWeek = today.minusWeeks(1);
        LocalDate last24Hours = today.minusDays(1);
        Date lastMonthDate = Date.from(lastMonth.atStartOfDay(ZoneOffset.UTC).toInstant());
        Date lastWeekDate = Date.from(lastWeek.atStartOfDay(ZoneOffset.UTC).toInstant());
        Date last24HoursDate = Date.from(last24Hours.atStartOfDay(ZoneOffset.UTC).toInstant());

        Integer lastMonthOrders = orderServiceRepository.countByCreatedAtAfter(lastMonthDate);
        Integer lastWeekOrders = orderServiceRepository.countByCreatedAtAfter(lastWeekDate);
        Integer last24HoursOrders = orderServiceRepository.countByCreatedAtAfter(last24HoursDate);
        log.info("Orders in the last month: {}", lastMonthOrders);
        log.info("Orders in the last week: {}", lastWeekOrders);
        log.info("Orders in the last 24 hours: {}", last24HoursOrders);
        Integer exitMonthOrders = orderServiceRepository.countByDepartureDateAfter(lastMonthDate);
        Integer exitWeekOrders = orderServiceRepository.countByDepartureDateAfter(lastWeekDate);
        Integer exit24HoursOrders = orderServiceRepository.countByDepartureDateAfter(last24HoursDate);

        OrderServiceAnalyticsResponse orderAnalytics = new OrderServiceAnalyticsResponse();
        orderAnalytics.setTotalOrders(orderServices);
        orderAnalytics.setLastMonthOrders(lastMonthOrders);
        orderAnalytics.setLastWeekOrders(lastWeekOrders);
        orderAnalytics.setLast24HoursOrders(last24HoursOrders);

        orderAnalytics.setExitOrders(orderExitServices);
        orderAnalytics.setExitOrdersLastMonth(exitMonthOrders);
        orderAnalytics.setExitOrdersLastWeek(exitWeekOrders);
        orderAnalytics.setExitOrdersLast24Hours(exit24HoursOrders);

        OrderServiceAnalyticsWeekResponse weekResponse = new OrderServiceAnalyticsWeekResponse();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date sunday = calendar.getTime();

        weekResponse.setEntrySunday(orderServiceRepository.findByCreatedAtDate(sunday).size());
        weekResponse.setExitSunday(orderServiceRepository.findByDepartureDateDate(sunday).size());

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        Date monday = calendar.getTime();
        weekResponse.setEntryMonday(orderServiceRepository.findByCreatedAtDate(monday).size());
        weekResponse.setExitMonday(orderServiceRepository.findByDepartureDateDate(monday).size());

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        Date tuesday = calendar.getTime();
        weekResponse.setEntryTuesday(orderServiceRepository.findByCreatedAtDate(tuesday).size());
        weekResponse.setExitTuesday(orderServiceRepository.findByDepartureDateDate(tuesday).size());

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        Date wednesday = calendar.getTime();
        weekResponse.setEntryWednesday(orderServiceRepository.findByCreatedAtDate(wednesday).size());
        weekResponse.setExitWednesday(orderServiceRepository.findByDepartureDateDate(wednesday).size());

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        Date thursday = calendar.getTime();
        weekResponse.setEntryThursday(orderServiceRepository.findByCreatedAtDate(thursday).size());
        weekResponse.setExitThursday(orderServiceRepository.findByDepartureDateDate(thursday).size());

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        Date friday = calendar.getTime();
        weekResponse.setEntryFriday(orderServiceRepository.findByCreatedAtDate(friday).size());
        weekResponse.setExitFriday(orderServiceRepository.findByDepartureDateDate(friday).size());

        calendar.add(Calendar.DAY_OF_WEEK, 1);
        Date saturday = calendar.getTime();
        weekResponse.setEntrySaturday(orderServiceRepository.findByCreatedAtDate(saturday).size());
        weekResponse.setExitSaturday(orderServiceRepository.findByDepartureDateDate(saturday).size());

        orderAnalytics.setOrderServiceAnalyticsWeekResponse(weekResponse);

        return orderAnalytics;
    }

    public OrderService getOrderServiceById(String orderServiceId) {
        log.info("Fetching order service with ID: {}", orderServiceId);
        return orderServiceRepository.findById(orderServiceId)
                .orElseThrow(() -> new InvalidOrderServiceIdException("Order service not found"));
    }

    public List<OrderService> getAllOrderServices() {
        log.info("Getting all order services");
        return orderServiceRepository.findAll();
    }

    public OrderServicePaginationResponse getOrderServicesPage(OrderServicePaginationRequest orderServicePaginationRequest) {
        log.info("Fetching order services page with request: {}", orderServicePaginationRequest);

        PageRequest pageRequest = PageRequest.of(orderServicePaginationRequest.page(), orderServicePaginationRequest.size());
        Page<OrderService> page;

        switch (orderServicePaginationRequest.searchType()) {
            case CLIENT -> page = orderServiceRepository.findByClientName(
                    orderServicePaginationRequest.searchValue() != null ? orderServicePaginationRequest.searchValue() : "", pageRequest);
            case SMARTPHONE -> page = orderServiceRepository.findBySmartphoneModel(
                    orderServicePaginationRequest.searchValue() != null ? orderServicePaginationRequest.searchValue() : "", pageRequest);
            case STATUS -> {
                Status status = orderServicePaginationRequest.searchValue() != null
                        ? getStatusOrThrow(orderServicePaginationRequest.searchValue())
                        : null;
                if (status != null) {
                    page = orderServiceRepository.findByStatus(status, pageRequest);
                } else {
                    page = orderServiceRepository.findAll(pageRequest);
                }
            }
            default -> page = orderServiceRepository.findAll(pageRequest);
        }

        return new OrderServicePaginationResponse(
                page.getTotalPages(),
                page.getNumber(),
                page.getContent(),
                page.getSize()
        );
    }

    private Status getStatusOrThrow(String status) {
        try {
            return Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

}
