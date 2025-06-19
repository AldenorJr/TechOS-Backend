package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.controller.advice.TechnicalAdvice;
import br.com.tech.os.ostech.exception.InvalidNameTechnicalAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidTechnicalFieldException;
import br.com.tech.os.ostech.exception.InvalidTechnicalIdException;
import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.Technical;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalCreateDTO;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import br.com.tech.os.ostech.service.TechnicalService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(TechnicalController.class)
class TechnicalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TechnicalService technicalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testInvalidTechnicalFieldExceptionHandler() {
        TechnicalAdvice advice = new TechnicalAdvice();
        String errorMsg = "Campo técnico inválido";
        InvalidTechnicalFieldException ex = new InvalidTechnicalFieldException(errorMsg);

        ResponseEntity<Map<String, String>> response = advice.handleInvalidTechnicalFieldException(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(errorMsg, response.getBody().get("message"));
    }

    @Test
    void createTechnical_success() throws Exception {
        TechnicalCreateDTO dto = new TechnicalCreateDTO("Novo Técnico");
        Technical technical = new Technical();
        technical.setId("1");
        technical.setName("Novo Técnico");

        Mockito.when(technicalService.createTechnical(any(TechnicalCreateDTO.class))).thenReturn(technical);

        mockMvc.perform(post("/v1/technicals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Novo Técnico"));
    }

    @Test
    void createTechnical_conflict() throws Exception {
        TechnicalCreateDTO dto = new TechnicalCreateDTO("Existente");

        Mockito.when(technicalService.createTechnical(any(TechnicalCreateDTO.class)))
                .thenThrow(new InvalidNameTechnicalAlreadyExistsException("Technical already exists"));

        mockMvc.perform(post("/v1/technicals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Technical already exists"));
    }

    @Test
    void updateTechnical_success() throws Exception {
        TechnicalUpdateDTO dto = new TechnicalUpdateDTO("Atualizado");
        Technical technical = new Technical();
        technical.setId("1");
        technical.setName("Atualizado");

        Mockito.when(technicalService.updateTechnical(eq("1"), any(TechnicalUpdateDTO.class))).thenReturn(technical);

        mockMvc.perform(put("/v1/technicals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Atualizado"));
    }

    @Test
    void updateTechnical_notFound() throws Exception {
        TechnicalUpdateDTO dto = new TechnicalUpdateDTO("Atualizado");

        Mockito.when(technicalService.updateTechnical(eq("1"), any(TechnicalUpdateDTO.class)))
                .thenThrow(new InvalidTechnicalIdException("Technical not found"));

        mockMvc.perform(put("/v1/technicals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Technical not found"));
    }

    @Test
    void getTechnicalById_success() throws Exception {
        Technical technical = new Technical();
        technical.setId("1");
        technical.setName("Técnico");

        Mockito.when(technicalService.getTechnicalById("1")).thenReturn(technical);

        mockMvc.perform(get("/v1/technicals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Técnico"));
    }

    @Test
    void getTechnicalById_notFound() throws Exception {
        Mockito.when(technicalService.getTechnicalById("1"))
                .thenThrow(new InvalidTechnicalIdException("Technical not found"));

        mockMvc.perform(get("/v1/technicals/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Technical not found"));
    }

    @Test
    void deleteTechnical_success() throws Exception {
        Mockito.doNothing().when(technicalService).deleteTechnical("1");

        mockMvc.perform(delete("/v1/technicals/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTechnical_notFound() throws Exception {
        Mockito.doThrow(new InvalidTechnicalIdException("Technical not found"))
                .when(technicalService).deleteTechnical("1");

        mockMvc.perform(delete("/v1/technicals/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Technical not found"));
    }

    @Test
    void getAllTechnicals_success() throws Exception {
        Technical t1 = new Technical();
        t1.setId("1");
        t1.setName("Técnico 1");
        Technical t2 = new Technical();
        t2.setId("2");
        t2.setName("Técnico 2");
        List<Technical> list = Arrays.asList(t1, t2);

        Mockito.when(technicalService.getAllTechnicals()).thenReturn(list);

        mockMvc.perform(get("/v1/technicals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }
}