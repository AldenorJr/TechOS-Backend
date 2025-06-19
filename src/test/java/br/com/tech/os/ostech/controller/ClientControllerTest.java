package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.enums.SearchType;
import br.com.tech.os.ostech.exception.InvalidClientFieldException;
import br.com.tech.os.ostech.exception.InvalidClientIdException;
import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientAnalyticsResponse;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientCreateDTO;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationRequest;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationResponse;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import br.com.tech.os.ostech.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClient_success() throws Exception {
        ClientCreateDTO dto = new ClientCreateDTO("Cliente Teste", "12345678900");
        Client client = new Client();
        client.setId("1");
        client.setName("Cliente Teste");
        client.setCpf("12345678900");

        Mockito.when(clientService.createClient(any(ClientCreateDTO.class))).thenReturn(client);

        mockMvc.perform(post("/v1/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Cliente Teste"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void updateClient_success() throws Exception {
        ClientCreateDTO dto = new ClientCreateDTO("Novo Nome", "98765432100");
        Client client = new Client();
        client.setId("1");
        client.setName("Novo Nome");
        client.setCpf("98765432100");

        Mockito.when(clientService.updateClient(eq("1"), any(ClientCreateDTO.class))).thenReturn(client);

        mockMvc.perform(put("/v1/client/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Novo Nome"))
                .andExpect(jsonPath("$.cpf").value("98765432100"));
    }

    @Test
    void deleteClient_success() throws Exception {
        Mockito.doNothing().when(clientService).deleteClient("1");

        mockMvc.perform(delete("/v1/client/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getClientById_success() throws Exception {
        Client client = new Client();
        client.setId("1");
        client.setName("Cliente Teste");
        client.setCpf("12345678900");

        Mockito.when(clientService.getClientById("1")).thenReturn(client);

        mockMvc.perform(get("/v1/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Cliente Teste"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void getClientAnalytics_success() throws Exception {
        ClientAnalyticsResponse analytics = new ClientAnalyticsResponse();
        analytics.setTotalClients(10);
        analytics.setLastMonthClients(5);
        analytics.setLastWeekClients(2);
        analytics.setLast24HoursClients(1);

        Mockito.when(clientService.getClientAnalytics()).thenReturn(analytics);

        mockMvc.perform(get("/v1/client/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClients").value(10))
                .andExpect(jsonPath("$.lastMonthClients").value(5))
                .andExpect(jsonPath("$.lastWeekClients").value(2))
                .andExpect(jsonPath("$.last24HoursClients").value(1));
    }

    @Test
    void getClientsPage_success() throws Exception {
        ClientPaginationResponse response = new ClientPaginationResponse();
        Mockito.when(clientService.getClientsPage(any(ClientPaginationRequest.class))).thenReturn(response);

        mockMvc.perform(get("/v1/client/pagination")
                        .param("page", "0")
                        .param("size", "10")
                        .param("searchType", SearchType.BLANK.name())
                        .param("searchValue", ""))
                .andExpect(status().isOk());
    }

    @Test
    void getAllClients_success() throws Exception {
        Client c1 = new Client();
        c1.setId("1");
        c1.setName("Cliente 1");
        c1.setCpf("111");
        Client c2 = new Client();
        c2.setId("2");
        c2.setName("Cliente 2");
        c2.setCpf("222");
        List<Client> list = Arrays.asList(c1, c2);

        Mockito.when(clientService.getAllClients()).thenReturn(list);

        mockMvc.perform(get("/v1/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

}