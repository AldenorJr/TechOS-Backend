package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.exception.InvalidContactIdException;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.Contact;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactCreateDTO;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactUpdateDTO;
import br.com.tech.os.ostech.repository.ClientRepository;
import br.com.tech.os.ostech.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContactService {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;

    public Contact createContact(String clientId, ContactCreateDTO contactCreateDTO) {
        log.info("Creating contact for client with ID: {}", clientId);
        Client client = clientService.getClientById(clientId);

        Contact contact = new Contact();
        contact.setEmail(contactCreateDTO.email());
        contact.setPhone(contactCreateDTO.phone());
        contact.setId(UUID.randomUUID().toString());
        client.setUpdatedAt(new Date());
        client.setContact(contact);

        clientRepository.save(client);

        return contactRepository.save(contact);
    }

    public Contact updateContact(String contactId, ContactUpdateDTO contactUpdateDTO) {
        log.info("Updating contact with ID: {}", contactId);
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new InvalidContactIdException("Contact not found"));

        contact.setEmail(contactUpdateDTO.email());
        contact.setPhone(contactUpdateDTO.phone());

        log.info("Updated contact with ID: {} to email: {} and phone: {}", contactId, contact.getEmail(), contact.getPhone());
        return contactRepository.save(contact);
    }

    public void deleteContact(String contactId) {
        log.info("Deleting contact with ID: {}", contactId);
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new InvalidContactIdException("Contact not found"));
        contactRepository.delete(contact);
        log.info("Deleted contact with ID: {}", contactId);
    }

    public Contact getContactById(String contactId) {
        log.info("Fetching contact with ID: {}", contactId);
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new InvalidContactIdException("Contact not found"));
    }

}
