package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.TestDummies;
import br.com.tech.os.ostech.enums.SearchType;
import br.com.tech.os.ostech.exception.InvalidClientIdException;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.Contact;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientAnalyticsResponse;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientCreateDTO;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationRequest;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationResponse;
import br.com.tech.os.ostech.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClient_shouldCreateClient() {
        ClientCreateDTO dto = new ClientCreateDTO("Cliente Teste", "12345678900");
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        Client result = clientService.createClient(dto);

        assertEquals(dto.name(), result.getName());
        assertEquals(dto.cpf(), result.getCpf());
        assertNotNull(result.getId());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void updateClient_shouldUpdateClient() {
        String id = "1";
        ClientCreateDTO dto = new ClientCreateDTO("Novo Nome", "98765432100");
        Client client = new Client();
        client.setId(id);
        client.setName("Antigo Nome");
        client.setCpf("11111111111");

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        Client result = clientService.updateClient(id, dto);

        assertEquals(dto.name(), result.getName());
        assertEquals(dto.cpf(), result.getCpf());
        verify(clientRepository).save(client);
    }

    @Test
    void updateClient_shouldThrowIfNotFound() {
        String id = "1";
        ClientCreateDTO dto = new ClientCreateDTO("Nome", "123");
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidClientIdException.class, () -> clientService.updateClient(id, dto));
    }

    @Test
    void deleteClient_shouldDeleteClient() {
        String id = "1";
        Client client = new Client();
        client.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        clientService.deleteClient(id);

        verify(clientRepository).delete(client);
    }

    @Test
    void deleteClient_shouldThrowIfNotFound() {
        String id = "1";
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidClientIdException.class, () -> clientService.deleteClient(id));
    }

    @Test
    void getClientById_shouldReturnClient() {
        String id = "1";
        Client client = new Client();
        client.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        Client result = clientService.getClientById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void getClientById_shouldThrowIfNotFound() {
        String id = "1";
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidClientIdException.class, () -> clientService.getClientById(id));
    }

    @Test
    void getAllClients_shouldReturnList() {
        List<Client> list = Arrays.asList(new Client(), new Client());
        when(clientRepository.findAll()).thenReturn(list);

        List<Client> result = clientService.getAllClients();

        assertEquals(2, result.size());
    }

    @Test
    void getClientAnalytics_shouldReturnCorrectCounts() {
        List<Client> allClients = Arrays.asList(new Client(), new Client(), new Client());

        when(clientRepository.findAll()).thenReturn(allClients);
        when(clientRepository.countByCreatedAtAfter(any(Date.class)))
                .thenReturn(2)
                .thenReturn(1)
                .thenReturn(1);

        ClientAnalyticsResponse analytics = clientService.getClientAnalytics();

        assertEquals(3, analytics.getTotalClients());
        assertEquals(2, analytics.getLastMonthClients());
        assertEquals(1, analytics.getLastWeekClients());
        assertEquals(1, analytics.getLast24HoursClients());
    }

    @Test
    void getClientAnalytics_shouldReturnZeroWhenNoClients() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());
        when(clientRepository.countByCreatedAtAfter(any(Date.class)))
                .thenReturn(0)
                .thenReturn(0)
                .thenReturn(0);

        ClientAnalyticsResponse analytics = clientService.getClientAnalytics();

        assertEquals(0, analytics.getTotalClients());
        assertEquals(0, analytics.getLastMonthClients());
        assertEquals(0, analytics.getLastWeekClients());
        assertEquals(0, analytics.getLast24HoursClients());
    }

    @Test
    void getClientsPage_shouldReturnByName() {
        ClientPaginationRequest req = new ClientPaginationRequest(0, 10, SearchType.NAME, "João");
        Client client = TestDummies.buildClient("1", "João Silva", "123", "joao@mail.com", "9999");
        Page<Client> page = new PageImpl<>(List.of(client), PageRequest.of(0, 10), 1);

        when(clientRepository.findByNameContainingIgnoreCase(eq("João"), any(Pageable.class))).thenReturn(page);

        ClientPaginationResponse resp = clientService.getClientsPage(req);

        assertEquals(1, resp.getTotalPages());
        assertEquals(1, resp.getClientsPage().size());
        assertEquals("João Silva", resp.getClientsPage().get(0).getName());
        verify(clientRepository).findByNameContainingIgnoreCase(eq("João"), any(Pageable.class));
    }

    @Test
    void getClientsPage_shouldReturnByPhone() {
        ClientPaginationRequest req = new ClientPaginationRequest(0, 10, SearchType.PHONE, "9999");
        Client client = TestDummies.buildClient("2", "Maria", "456", "maria@mail.com", "9999");
        Page<Client> page = new PageImpl<>(List.of(client), PageRequest.of(0, 10), 1);

        when(clientRepository.findByContactPhoneContainingIgnoreCase(eq("9999"), any(Pageable.class))).thenReturn(page);

        ClientPaginationResponse resp = clientService.getClientsPage(req);

        assertEquals(1, resp.getTotalPages());
        assertEquals(1, resp.getClientsPage().size());
        assertEquals("Maria", resp.getClientsPage().get(0).getName());
        verify(clientRepository).findByContactPhoneContainingIgnoreCase(eq("9999"), any(Pageable.class));
    }

    @Test
    void getClientsPage_shouldReturnByEmail() {
        ClientPaginationRequest req = new ClientPaginationRequest(0, 10, SearchType.EMAIL, "mail@");
        Client client = TestDummies.buildClient("3", "Carlos", "789", "carlos@mail.com", "8888");
        Page<Client> page = new PageImpl<>(List.of(client), PageRequest.of(0, 10), 1);

        when(clientRepository.findByContactEmailContainingIgnoreCase(eq("mail@"), any(Pageable.class))).thenReturn(page);

        ClientPaginationResponse resp = clientService.getClientsPage(req);

        assertEquals(1, resp.getTotalPages());
        assertEquals(1, resp.getClientsPage().size());
        assertEquals("Carlos", resp.getClientsPage().get(0).getName());
        verify(clientRepository).findByContactEmailContainingIgnoreCase(eq("mail@"), any(Pageable.class));
    }

    @Test
    void getClientsPage_shouldReturnAllWhenBlank() {
        ClientPaginationRequest req = new ClientPaginationRequest(0, 10, SearchType.BLANK, "");
        Client client = TestDummies.buildClient("4", "Ana", "321", "ana@mail.com", "7777");
        Page<Client> page = new PageImpl<>(List.of(client), PageRequest.of(0, 10), 1);

        when(clientRepository.findAll(any(Pageable.class))).thenReturn(page);

        ClientPaginationResponse resp = clientService.getClientsPage(req);

        assertEquals(1, resp.getTotalPages());
        assertEquals(1, resp.getClientsPage().size());
        assertEquals("Ana", resp.getClientsPage().get(0).getName());
        verify(clientRepository).findAll(any(Pageable.class));
    }

}