package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.controller.advice.ContactAdvice;
import br.com.tech.os.ostech.exception.InvalidContactIdException;
import br.com.tech.os.ostech.exception.InvalidContactInformationException;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.Contact;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactCreateDTO;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactUpdateDTO;
import br.com.tech.os.ostech.repository.ClientRepository;
import br.com.tech.os.ostech.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class ContactServiceTest {

    @Mock
    private ClientService clientService;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createContact_shouldCreateContact() {
        String clientId = "client-1";
        ContactCreateDTO dto = new ContactCreateDTO("email@teste.com", "123456789");
        Client client = new Client();
        client.setId(clientId);

        when(clientService.getClientById(clientId)).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));
        when(contactRepository.save(any(Contact.class))).thenAnswer(i -> {
            Contact c = i.getArgument(0);
            if (c.getId() == null) c.setId(UUID.randomUUID().toString());
            return c;
        });

        Contact result = contactService.createContact(clientId, dto);

        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.phone(), result.getPhone());
        assertNotNull(result.getId());
        verify(clientRepository).save(any(Client.class));
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    void updateContact_shouldUpdateContact() {
        String contactId = UUID.randomUUID().toString();
        ContactUpdateDTO dto = new ContactUpdateDTO("novo@teste.com", "987654321");
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setEmail("antigo@teste.com");
        contact.setPhone("111111111");

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));
        when(contactRepository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

        Contact result = contactService.updateContact(contactId, dto);

        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.phone(), result.getPhone());
        verify(contactRepository).save(contact);
    }

    @Test
    void updateContact_shouldThrowIfNotFound() {
        String contactId = UUID.randomUUID().toString();
        ContactUpdateDTO dto = new ContactUpdateDTO("novo@teste.com", "987654321");

        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        assertThrows(InvalidContactIdException.class, () -> contactService.updateContact(contactId, dto));
    }

    @Test
    void deleteContact_shouldDeleteContact() {
        String contactId = UUID.randomUUID().toString();
        Contact contact = new Contact();
        contact.setId(contactId);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));
        doNothing().when(contactRepository).delete(contact);

        contactService.deleteContact(contactId);

        verify(contactRepository).delete(contact);
    }

    @Test
    void deleteContact_shouldThrowIfNotFound() {
        String contactId = UUID.randomUUID().toString();
        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        assertThrows(InvalidContactIdException.class, () -> contactService.deleteContact(contactId));
    }

    @Test
    void getContactById_shouldReturnContact() {
        String contactId = UUID.randomUUID().toString();
        Contact contact = new Contact();
        contact.setId(contactId);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));

        Contact result = contactService.getContactById(contactId);

        assertEquals(contactId, result.getId());
    }

    @Test
    void getContactById_shouldThrowIfNotFound() {
        String contactId = UUID.randomUUID().toString();
        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        assertThrows(InvalidContactIdException.class, () -> contactService.getContactById(contactId));
    }

    @Test
    void testInvalidContactInformationExceptionHandler() {
        ContactAdvice advice = new ContactAdvice();
        String errorMsg = "Informação de contato inválida";
        InvalidContactInformationException ex = new InvalidContactInformationException(errorMsg);

        ResponseEntity<Map<String, String>> response = advice.InvalidContactInformationException(ex);

        assertEquals(409, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(errorMsg, response.getBody().get("message"));
    }

}