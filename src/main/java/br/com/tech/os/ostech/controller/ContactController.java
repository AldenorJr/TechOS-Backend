package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.model.Contact;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactCreateDTO;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactUpdateDTO;
import br.com.tech.os.ostech.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/contact")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/{clientId}")
    public ResponseEntity<Contact> createContact(@RequestBody ContactCreateDTO contactCreateDTO,
                                                        @PathVariable String clientId) {
        Contact contact = contactService.createContact(clientId, contactCreateDTO);
        contact.setCreatedAt(new Date());
        contact.setUpdatedAt(new Date());
        return ResponseEntity.status(201).body(contact);
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<Contact> updateContact(@PathVariable String contactId,
                                                 @RequestBody ContactUpdateDTO contactUpdateDTO) {
        Contact contact = contactService.updateContact(contactId, contactUpdateDTO);
        contact.setUpdatedAt(new Date());
        return ResponseEntity.ok(contact);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable String contactId) {
        contactService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<Contact> getContactById(@PathVariable String contactId) {
        Contact contact = contactService.getContactById(contactId);
        return ResponseEntity.ok(contact);
    }


}
