package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.enums.OrderServiceSearchType;
import br.com.tech.os.ostech.exception.InvalidOrderServiceIdException;
import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.*;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServiceCreateDTO;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServicePaginationRequest;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServicePaginationResponse;
import br.com.tech.os.ostech.model.dto.orderServiceDTO.OrderServiceUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import br.com.tech.os.ostech.service.OrderServiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(OrderServiceController.class)
class OrderServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderServiceService orderServiceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarOrderServiceComSucesso() throws Exception {
        OrderServiceCreateDTO dto = new OrderServiceCreateDTO("smart-1", "client-1", "budget-1", "OPEN");
        OrderService os = new OrderService();
        os.setId(1);
        os.setStatus(Status.OPEN);

        Mockito.when(orderServiceService.createOrderService(any(OrderServiceCreateDTO.class))).thenReturn(os);

        mockMvc.perform(post("/v1/order-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void deveBuscarOrderServicePorIdComSucesso() throws Exception {
        OrderService os = new OrderService();
        os.setId(1);
        os.setStatus(Status.OPEN);

        Mockito.when(orderServiceService.getOrderServiceById("1")).thenReturn(os);

        mockMvc.perform(get("/v1/order-service/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void deveLancar404AoBuscarOrderServiceInexistente() throws Exception {
        Mockito.when(orderServiceService.getOrderServiceById("99"))
                .thenThrow(new InvalidOrderServiceIdException("OrderService não encontrado"));

        mockMvc.perform(get("/v1/order-service/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("OrderService não encontrado"));
    }

    @Test
    void deveAtualizarOrderServiceComSucesso() throws Exception {
        OrderServiceUpdateDTO dto = new OrderServiceUpdateDTO("smart-1", "budget-1", "FINISHED", "client-1", null);
        OrderService os = new OrderService();
        os.setId(1);
        os.setStatus(Status.FINISHED);

        Mockito.when(orderServiceService.updateOrderService(eq("1"), any(OrderServiceUpdateDTO.class))).thenReturn(os);

        mockMvc.perform(put("/v1/order-service/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("FINISHED"));
    }

    @Test
    void deveLancar404AoAtualizarOrderServiceInexistente() throws Exception {
        OrderServiceUpdateDTO dto = new OrderServiceUpdateDTO("smart-1", "budget-1", "FINISHED", "client-1", null);

        Mockito.when(orderServiceService.updateOrderService(eq("99"), any(OrderServiceUpdateDTO.class)))
                .thenThrow(new InvalidOrderServiceIdException("OrderService não encontrado"));

        mockMvc.perform(put("/v1/order-service/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("OrderService não encontrado"));
    }

    @Test
    void deveDeletarOrderServiceComSucesso() throws Exception {
        Mockito.doNothing().when(orderServiceService).deleteOrderService("1");

        mockMvc.perform(delete("/v1/order-service/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveLancar404AoDeletarOrderServiceInexistente() throws Exception {
        Mockito.doThrow(new InvalidOrderServiceIdException("OrderService não encontrado"))
                .when(orderServiceService).deleteOrderService("99");

        mockMvc.perform(delete("/v1/order-service/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("OrderService não encontrado"));
    }

    @Test
    void deveBuscarTodosOrderServicesComSucesso() throws Exception {
        OrderService os1 = new OrderService();
        os1.setId(1);
        os1.setStatus(Status.OPEN);
        OrderService os2 = new OrderService();
        os2.setId(2);
        os2.setStatus(Status.FINISHED);

        Mockito.when(orderServiceService.getAllOrderServices()).thenReturn(List.of(os1, os2));

        mockMvc.perform(get("/v1/order-service/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveBuscarOrderServicesPaginadosComSucesso() throws Exception {
        OrderServicePaginationResponse resp = new OrderServicePaginationResponse(1, 0, List.of(), 10);
        Mockito.when(orderServiceService.getOrderServicesPage(any(OrderServicePaginationRequest.class))).thenReturn(resp);

        mockMvc.perform(get("/v1/order-service/pagination")
                        .param("page", "0")
                        .param("size", "10")
                        .param("searchType", OrderServiceSearchType.BLANK.name())
                        .param("searchValue", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.pageSize").value(10));
    }
}