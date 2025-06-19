package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.exception.InvalidSmartphoneIdException;
import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.Smartphone;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneCreateDTO;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import br.com.tech.os.ostech.service.SmartphoneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(SmartphoneController.class)
class SmartphoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SmartphoneService smartphoneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarSmartphoneComSucesso() throws Exception {
        SmartphoneCreateDTO dto = new SmartphoneCreateDTO("Galaxy S22", "Não liga", "Sem riscos", "1234567890");
        Smartphone smartphone = new Smartphone();
        smartphone.setId("smart-1");
        smartphone.setModel("Galaxy S22");
        smartphone.setProblem("Não liga");
        smartphone.setObservation("Sem riscos");

        Mockito.when(smartphoneService.createSmartphone(any(SmartphoneCreateDTO.class))).thenReturn(smartphone);

        mockMvc.perform(post("/v1/smartphone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("smart-1"))
                .andExpect(jsonPath("$.model").value("Galaxy S22"))
                .andExpect(jsonPath("$.problem").value("Não liga"))
                .andExpect(jsonPath("$.observation").value("Sem riscos"));
    }

    @Test
    void deveAtualizarSmartphoneComSucesso() throws Exception {
        SmartphoneUpdateDTO dto = new SmartphoneUpdateDTO("iPhone 13", "Quebrou tela", "Novo", "1234567890");
        Smartphone smartphone = new Smartphone();
        smartphone.setId("smart-1");
        smartphone.setModel("iPhone 13");
        smartphone.setProblem("Quebrou tela");
        smartphone.setObservation("Novo");

        Mockito.when(smartphoneService.updateSmartphone(eq("smart-1"), any(SmartphoneUpdateDTO.class))).thenReturn(smartphone);

        mockMvc.perform(put("/v1/smartphone/smart-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("smart-1"))
                .andExpect(jsonPath("$.model").value("iPhone 13"))
                .andExpect(jsonPath("$.problem").value("Quebrou tela"))
                .andExpect(jsonPath("$.observation").value("Novo"));
    }

    @Test
    void deveLancar404AoAtualizarSmartphoneInexistente() throws Exception {
        SmartphoneUpdateDTO dto = new SmartphoneUpdateDTO("iPhone 13", "Quebrou tela", "Novo", "1234567890");

        Mockito.when(smartphoneService.updateSmartphone(eq("not-found"), any(SmartphoneUpdateDTO.class)))
                .thenThrow(new InvalidSmartphoneIdException("Smartphone não encontrado"));

        mockMvc.perform(put("/v1/smartphone/not-found")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Smartphone não encontrado"));
    }

    @Test
    void deveDeletarSmartphoneComSucesso() throws Exception {
        Mockito.doNothing().when(smartphoneService).deleteSmartphone("smart-1");

        mockMvc.perform(delete("/v1/smartphone/smart-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveLancar404AoDeletarSmartphoneInexistente() throws Exception {
        Mockito.doThrow(new InvalidSmartphoneIdException("Smartphone não encontrado"))
                .when(smartphoneService).deleteSmartphone("not-found");

        mockMvc.perform(delete("/v1/smartphone/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Smartphone não encontrado"));
    }

    @Test
    void deveBuscarSmartphonePorIdComSucesso() throws Exception {
        Smartphone smartphone = new Smartphone();
        smartphone.setId("smart-1");
        smartphone.setModel("Galaxy S22");
        smartphone.setProblem("Não liga");
        smartphone.setObservation("Sem riscos");

        Mockito.when(smartphoneService.getSmartphoneById("smart-1")).thenReturn(smartphone);

        mockMvc.perform(get("/v1/smartphone/smart-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("smart-1"))
                .andExpect(jsonPath("$.model").value("Galaxy S22"))
                .andExpect(jsonPath("$.problem").value("Não liga"))
                .andExpect(jsonPath("$.observation").value("Sem riscos"));
    }

    @Test
    void deveLancar404AoBuscarSmartphoneInexistente() throws Exception {
        Mockito.when(smartphoneService.getSmartphoneById("not-found"))
                .thenThrow(new InvalidSmartphoneIdException("Smartphone não encontrado"));

        mockMvc.perform(get("/v1/smartphone/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Smartphone não encontrado"));
    }
}