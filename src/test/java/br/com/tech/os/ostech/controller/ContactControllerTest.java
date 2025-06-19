package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.controller.advice.TechnicalAdvice;
import br.com.tech.os.ostech.exception.InvalidContactIdException;
import br.com.tech.os.ostech.exception.InvalidTechnicalFieldException;
import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.Contact;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactCreateDTO;
import br.com.tech.os.ostech.model.dto.contactDTO.ContactUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import br.com.tech.os.ostech.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createContact_success() throws Exception {
        ContactCreateDTO dto = new ContactCreateDTO("email@teste.com", "123456789");
        Contact contact = new Contact();
        contact.setId("1");
        contact.setEmail("email@teste.com");
        contact.setPhone("123456789");

        Mockito.when(contactService.createContact(eq("10"), any(ContactCreateDTO.class))).thenReturn(contact);

        mockMvc.perform(post("/v1/contact/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@teste.com"))
                .andExpect(jsonPath("$.phone").value("123456789"));
    }

    @Test
    void handleInvalidTechnicalFieldException_deveRetornarStatus409EMessageCorreta() {
        TechnicalAdvice advice = new TechnicalAdvice();
        String errorMsg = "Campo técnico inválido";
        InvalidTechnicalFieldException ex = new InvalidTechnicalFieldException(errorMsg);

        ResponseEntity<Map<String, String>> response = advice.handleInvalidTechnicalFieldException(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(errorMsg, response.getBody().get("message"));
    }

    @Test
    void updateContact_success() throws Exception {
        ContactUpdateDTO dto = new ContactUpdateDTO("novo@teste.com", "987654321");
        Contact contact = new Contact();
        contact.setId("1");
        contact.setEmail("novo@teste.com");
        contact.setPhone("987654321");

        Mockito.when(contactService.updateContact(eq("1"), any(ContactUpdateDTO.class))).thenReturn(contact);

        mockMvc.perform(put("/v1/contact/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("novo@teste.com"))
                .andExpect(jsonPath("$.phone").value("987654321"));
    }

    @Test
    void updateContact_notFound() throws Exception {
        ContactUpdateDTO dto = new ContactUpdateDTO("novo@teste.com", "987654321");

        Mockito.when(contactService.updateContact(eq("1"), any(ContactUpdateDTO.class)))
                .thenThrow(new InvalidContactIdException("Contact not found"));

        mockMvc.perform(put("/v1/contact/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contact not found"));
    }

    @Test
    void deleteContact_success() throws Exception {
        Mockito.doNothing().when(contactService).deleteContact("1");

        mockMvc.perform(delete("/v1/contact/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteContact_notFound() throws Exception {
        Mockito.doThrow(new InvalidContactIdException("Contact not found"))
                .when(contactService).deleteContact("1");

        mockMvc.perform(delete("/v1/contact/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contact not found"));
    }

    @Test
    void getContactById_success() throws Exception {
        Contact contact = new Contact();
        contact.setId("1");
        contact.setEmail("email@teste.com");
        contact.setPhone("123456789");

        Mockito.when(contactService.getContactById("1")).thenReturn(contact);

        mockMvc.perform(get("/v1/contact/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@teste.com"))
                .andExpect(jsonPath("$.phone").value("123456789"));
    }

    @Test
    void getContactById_notFound() throws Exception {
        Mockito.when(contactService.getContactById("1"))
                .thenThrow(new InvalidContactIdException("Contact not found"));

        mockMvc.perform(get("/v1/contact/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contact not found"));
    }
}