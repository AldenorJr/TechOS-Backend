package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.enums.OrderServiceSearchType;
import br.com.tech.os.ostech.exception.InvalidOrderServiceIdException;
import br.com.tech.os.ostech.model.*;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServiceCreateDTO;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServicePaginationRequest;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServicePaginationResponse;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServiceUpdateDTO;
import br.com.tech.os.ostech.repository.OrderServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceServiceTest {

    @Mock
    private OrderServiceRepository orderServiceRepository;
    @Mock
    private SmartphoneService smartphoneService;
    @Mock
    private ClientService clientService;
    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private OrderServiceService orderServiceService;

    private Client client;
    private Smartphone smartphone;
    private Budget budget;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId("client-1");
        smartphone = new Smartphone();
        smartphone.setId("smart-1");
        budget = new Budget();
        budget.setId("budget-1");
        orderService = new OrderService();
        orderService.setId(1);
        orderService.setClientId(client);
        orderService.setSmartphoneId(smartphone);
        orderService.setBudgetId(budget);
        orderService.setStatus(Status.OPEN);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar OrderService inexistente (linha 53)")
    void deleteOrderServiceShouldThrowWhenNotFound2() {
        when(orderServiceRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(InvalidOrderServiceIdException.class, () -> orderServiceService.deleteOrderService("999"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar OrderService inexistente por ID (linha 72)")
    void getOrderServiceByIdShouldThrowWhenNotFound2() {
        when(orderServiceRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(InvalidOrderServiceIdException.class, () -> orderServiceService.getOrderServiceById("999"));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para status inválido (linha 81 e 132)")
    void getStatusOrThrowShouldThrowForInvalidStatus() throws Exception {
        var method = OrderServiceService.class.getDeclaredMethod("getStatusOrThrow", String.class);
        method.setAccessible(true);
        Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            method.invoke(orderServiceService, "INVALID_STATUS");
        });
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Invalid status: INVALID_STATUS", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Deve retornar Status correto para status válido (linha 85)")
    void getStatusOrThrowShouldReturnStatusForValidStatus() throws Exception {
        var method = OrderServiceService.class.getDeclaredMethod("getStatusOrThrow", String.class);
        method.setAccessible(true);
        Object result = method.invoke(orderServiceService, "OPEN");
        assertEquals(Status.OPEN, result);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao buscar página por status inválido (linha 132 via getOrderServicesPage)")
    void getOrderServicesPageShouldThrowForInvalidStatus() {
        OrderServicePaginationRequest req = new OrderServicePaginationRequest(0, 10, OrderServiceSearchType.STATUS, "INVALID_STATUS");
        assertThrows(IllegalArgumentException.class, () -> orderServiceService.getOrderServicesPage(req));
    }

    @Test
    @DisplayName("Deve criar OrderService com sucesso")
    void createOrderServiceShouldSucceed() {
        OrderServiceCreateDTO dto = new OrderServiceCreateDTO("smart-1", "client-1", "budget-1", "OPEN");
        when(clientService.getClientById("client-1")).thenReturn(client);
        when(smartphoneService.getSmartphoneById("smart-1")).thenReturn(smartphone);
        when(budgetService.getBudgetById("budget-1")).thenReturn(budget);
        when(orderServiceRepository.save(any(OrderService.class))).thenReturn(orderService);

        OrderService result = orderServiceService.createOrderService(dto);

        assertEquals(client, result.getClientId());
        assertEquals(smartphone, result.getSmartphoneId());
        assertEquals(budget, result.getBudgetId());
        assertEquals(Status.OPEN, result.getStatus());
        verify(orderServiceRepository).save(any(OrderService.class));
    }

    @Test
    @DisplayName("Deve criar OrderService sem budget")
    void createOrderServiceWithoutBudget() {
        OrderServiceCreateDTO dto = new OrderServiceCreateDTO("smart-1", "client-1", null, "FINISHED");
        when(clientService.getClientById("client-1")).thenReturn(client);
        when(smartphoneService.getSmartphoneById("smart-1")).thenReturn(smartphone);

        OrderService orderServiceWithoutBudget = new OrderService();
        orderServiceWithoutBudget.setId(2);
        orderServiceWithoutBudget.setClientId(client);
        orderServiceWithoutBudget.setSmartphoneId(smartphone);
        orderServiceWithoutBudget.setBudgetId(null);
        orderServiceWithoutBudget.setStatus(Status.OPEN);

        when(orderServiceRepository.save(any(OrderService.class))).thenReturn(orderServiceWithoutBudget);

        OrderService result = orderServiceService.createOrderService(dto);

        assertEquals(Status.OPEN, result.getStatus());
        assertNull(result.getBudgetId());
        verify(orderServiceRepository).save(any(OrderService.class));
    }

    @Test
    @DisplayName("Deve atualizar OrderService com sucesso")
    void updateOrderServiceShouldSucceed() {
        OrderServiceUpdateDTO dto = new OrderServiceUpdateDTO("smart-1", "budget-1", "FINISHED", "client-1", null);
        when(orderServiceRepository.findById("1")).thenReturn(Optional.of(orderService));
        when(clientService.getClientById("client-1")).thenReturn(client);
        when(smartphoneService.getSmartphoneById("smart-1")).thenReturn(smartphone);
        when(budgetService.getBudgetById("budget-1")).thenReturn(budget);
        when(orderServiceRepository.save(any(OrderService.class))).thenReturn(orderService);

        OrderService result = orderServiceService.updateOrderService("1", dto);

        assertEquals(Status.FINISHED, result.getStatus());
        verify(orderServiceRepository).save(orderService);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar OrderService inexistente")
    void updateOrderServiceShouldThrowWhenNotFound() {
        OrderServiceUpdateDTO dto = new OrderServiceUpdateDTO("client-1", "smart-1", "budget-1", "OPEN", null);
        when(orderServiceRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(InvalidOrderServiceIdException.class, () -> orderServiceService.updateOrderService("99", dto));
        verify(orderServiceRepository).findById("99");
    }

    @Test
    @DisplayName("Deve deletar OrderService com sucesso")
    void deleteOrderServiceShouldSucceed() {
        when(orderServiceRepository.findById("1")).thenReturn(Optional.of(orderService));
        doNothing().when(orderServiceRepository).delete(orderService);

        orderServiceService.deleteOrderService("1");

        verify(orderServiceRepository).delete(orderService);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar OrderService inexistente")
    void deleteOrderServiceShouldThrowWhenNotFound() {
        when(orderServiceRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(InvalidOrderServiceIdException.class, () -> orderServiceService.deleteOrderService("99"));
        verify(orderServiceRepository).findById("99");
    }

    @Test
    @DisplayName("Deve buscar OrderService por ID com sucesso")
    void getOrderServiceByIdShouldSucceed() {
        when(orderServiceRepository.findById("1")).thenReturn(Optional.of(orderService));

        OrderService result = orderServiceService.getOrderServiceById("1");

        assertEquals(orderService, result);
        verify(orderServiceRepository).findById("1");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar OrderService inexistente")
    void getOrderServiceByIdShouldThrowWhenNotFound() {
        when(orderServiceRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(InvalidOrderServiceIdException.class, () -> orderServiceService.getOrderServiceById("99"));
        verify(orderServiceRepository).findById("99");
    }

    @Test
    @DisplayName("Deve buscar todos os OrderServices")
    void getAllOrderServicesShouldReturnList() {
        when(orderServiceRepository.findAll()).thenReturn(List.of(orderService));

        List<OrderService> result = orderServiceService.getAllOrderServices();

        assertEquals(1, result.size());
        verify(orderServiceRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar OrderServices paginados por CLIENT")
    void getOrderServicesPageByClient() {
        OrderServicePaginationRequest req = new OrderServicePaginationRequest(0, 10, OrderServiceSearchType.CLIENT, "John");
        Page<OrderService> page = new PageImpl<>(List.of(orderService), PageRequest.of(0, 10), 1);
        when(orderServiceRepository.findByClientName(anyString(), any(PageRequest.class))).thenReturn(page);

        OrderServicePaginationResponse resp = orderServiceService.getOrderServicesPage(req);

        assertEquals(1, resp.getTotalPages());
        assertEquals(0, resp.getCurrentPage());
        assertEquals(10, resp.getPageSize());
    }

    @Test
    @DisplayName("Deve buscar OrderServices paginados por SMARTPHONE")
    void getOrderServicesPageBySmartphone() {
        OrderServicePaginationRequest req = new OrderServicePaginationRequest(0, 10, OrderServiceSearchType.SMARTPHONE, "Galaxy");
        Page<OrderService> page = new PageImpl<>(List.of(orderService), PageRequest.of(0, 10), 1);
        when(orderServiceRepository.findBySmartphoneModel(anyString(), any(PageRequest.class))).thenReturn(page);

        OrderServicePaginationResponse resp = orderServiceService.getOrderServicesPage(req);

        assertEquals(1, resp.getTotalPages());
        assertEquals(10, resp.getPageSize());
    }

    @Test
    @DisplayName("Deve buscar OrderServices paginados por STATUS")
    void getOrderServicesPageByStatus() {
        OrderServicePaginationRequest req = new OrderServicePaginationRequest(0, 10, OrderServiceSearchType.STATUS, "OPEN");
        Page<OrderService> page = new PageImpl<>(List.of(orderService), PageRequest.of(0, 10), 1);
        when(orderServiceRepository.findByStatus(eq(Status.OPEN), any(PageRequest.class))).thenReturn(page);

        OrderServicePaginationResponse resp = orderServiceService.getOrderServicesPage(req);

        assertEquals(10, resp.getPageSize());
    }

    @Test
    @DisplayName("Deve buscar OrderServices paginados default")
    void getOrderServicesPageDefault() {
        OrderServicePaginationRequest req = new OrderServicePaginationRequest(0, 10, OrderServiceSearchType.BLANK, null);
        Page<OrderService> page = new PageImpl<>(List.of(orderService), PageRequest.of(0, 10), 1);
        when(orderServiceRepository.findAll(any(PageRequest.class))).thenReturn(page);

        OrderServicePaginationResponse resp = orderServiceService.getOrderServicesPage(req);

        assertEquals(10, resp.getPageSize());
    }

    @Test
    @DisplayName("Deve lançar exceção para status inválido")
    void getOrderServicesPageInvalidStatus() {
        OrderServicePaginationRequest req = new OrderServicePaginationRequest(0, 10, OrderServiceSearchType.STATUS, "INVALID");
        assertThrows(IllegalArgumentException.class, () -> orderServiceService.getOrderServicesPage(req));
    }
}