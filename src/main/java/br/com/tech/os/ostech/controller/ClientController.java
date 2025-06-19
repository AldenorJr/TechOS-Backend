package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.enums.SearchType;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientAnalyticsResponse;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientCreateDTO;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationRequest;
import br.com.tech.os.ostech.model.dto.clientDTO.ClientPaginationResponse;
import br.com.tech.os.ostech.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/client")
public class ClientController {

    private final ClientService clientService;

    @PostMapping()
    public ResponseEntity<Client> createClient(@RequestBody ClientCreateDTO clientCreateDTO) {
        Client client = clientService.createClient(clientCreateDTO);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable String id, @RequestBody ClientCreateDTO clientCreateDTO) {
        Client client = clientService.updateClient(id, clientCreateDTO);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/analytics")
    public ResponseEntity<ClientAnalyticsResponse> getClientAnalytics() {
        ClientAnalyticsResponse analytics = clientService.getClientAnalytics();
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/pagination")
    public ResponseEntity<ClientPaginationResponse> getClientsPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "searchType", defaultValue = "BLANK") SearchType searchType,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue) {
        ClientPaginationRequest clientPaginationRequest = new ClientPaginationRequest(page, size, searchType, searchValue);
        ClientPaginationResponse paginationResponse = clientService.getClientsPage(clientPaginationRequest);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping()
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

}
