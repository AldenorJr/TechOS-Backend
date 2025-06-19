package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.exception.InvalidClientIdException;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientAnalyticsResponse;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientCreateDTO;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationRequest;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationResponse;
import br.com.tech.os.ostech.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(ClientCreateDTO clientCreateDTO) {
        Client client = new Client();

        client.setName(clientCreateDTO.name());
        client.setCpf(clientCreateDTO.cpf());
        client.setId(UUID.randomUUID().toString());
        client.setCreatedAt(new Date());
        client.setUpdatedAt(new Date());

        log.info("Creating client with name: {} and CPF: {}", client.getName(), client.getCpf());
        return clientRepository.save(client);
    }

    public ClientAnalyticsResponse getClientAnalytics() {
        log.info("Buscando analytics de clientes");
        List<Client> clients = clientRepository.findAll();
        Integer totalClients = clients.size();
        log.info("Total clients: {}", totalClients);

        LocalDate today = LocalDate.now();
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate lastWeek = today.minusWeeks(1);
        LocalDate last24Hours = today.minusDays(1);
        Date lastMonthDate = Date.from(lastMonth.atStartOfDay(ZoneOffset.UTC).toInstant());
        Date lastWeekDate = Date.from(lastWeek.atStartOfDay(ZoneOffset.UTC).toInstant());
        Date last24HoursDate = Date.from(last24Hours.atStartOfDay(ZoneOffset.UTC).toInstant());

        Integer lastMonthClients = clientRepository.countByCreatedAtAfter(lastMonthDate);
        Integer lastWeekClients = clientRepository.countByCreatedAtAfter(lastWeekDate);
        Integer last24HoursClients = clientRepository.countByCreatedAtAfter(last24HoursDate);

        ClientAnalyticsResponse clientAnalytics = new ClientAnalyticsResponse();
        clientAnalytics.setTotalClients(totalClients);
        clientAnalytics.setLastMonthClients(lastMonthClients);
        clientAnalytics.setLastWeekClients(lastWeekClients);
        clientAnalytics.setLast24HoursClients(last24HoursClients);

        return clientAnalytics;
    }

    public ClientPaginationResponse getClientsPage(ClientPaginationRequest clientPaginationRequest) {
        log.info("Fetching clients page with request: {}", clientPaginationRequest);

        PageRequest pageRequest = PageRequest.of(clientPaginationRequest.page(), clientPaginationRequest.size());
        Page<Client> page;

        switch (clientPaginationRequest.searchType()) {
            case NAME -> page = clientRepository.findByNameContainingIgnoreCase(
                    clientPaginationRequest.searchValue() != null ? clientPaginationRequest.searchValue() : "", pageRequest);
            case PHONE -> page = clientRepository.findByContactPhoneContainingIgnoreCase(
                    clientPaginationRequest.searchValue() != null ? clientPaginationRequest.searchValue() : "", pageRequest);
            case EMAIL -> page = clientRepository.findByContactEmailContainingIgnoreCase(
                    clientPaginationRequest.searchValue() != null ? clientPaginationRequest.searchValue() : "", pageRequest);
            default -> page = clientRepository.findAll(pageRequest);
        }

        return new ClientPaginationResponse(
                page.getTotalPages(),
                page.getNumber(),
                page.getContent(),
                page.getSize()
        );
    }

    public Client updateClient(String id, ClientCreateDTO clientCreateDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new InvalidClientIdException("Client not found"));

        client.setName(clientCreateDTO.name());
        client.setCpf(clientCreateDTO.cpf());
        client.setUpdatedAt(new Date());

        log.info("Updating client with ID: {} to name: {} and CPF: {}", id, client.getName(), client.getCpf());
        return clientRepository.save(client);
    }

    public void deleteClient(String id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new InvalidClientIdException("Client not found"));
        log.info("Deleting client with ID: {}", id);
        clientRepository.delete(client);
    }

    public Client getClientById(String id) {
        log.info("Fetching client with ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new InvalidClientIdException("Client not found"));
    }

    public List<Client> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }

}
