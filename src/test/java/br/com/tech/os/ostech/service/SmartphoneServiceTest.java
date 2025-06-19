package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.exception.InvalidSmartphoneIdException;
import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.Smartphone;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneCreateDTO;
import br.com.tech.os.ostech.model.dto.smartphoneDTO.SmartphoneUpdateDTO;
import br.com.tech.os.ostech.repository.SmartphoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmartphoneServiceTest {

    @Mock
    private SmartphoneRepository smartphoneRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private SmartphoneService smartphoneService;

    private Client client;
    private Smartphone smartphone;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId("client-1");
        smartphone = new Smartphone();
        smartphone.setId("smart-1");
        smartphone.setModel("Galaxy S22");
        smartphone.setProblem("Não liga");
        smartphone.setObservation("Sem riscos");
    }

    @Test
    @DisplayName("Deve criar smartphone com sucesso")
    void createSmartphoneShouldSucceed() {
        SmartphoneCreateDTO dto = new SmartphoneCreateDTO("Galaxy S22", "Não liga", "Sem riscos", "123456789");

        when(smartphoneRepository.save(any(Smartphone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Smartphone result = smartphoneService.createSmartphone( dto);

        assertEquals(dto.model(), result.getModel());
        assertEquals(dto.problem(), result.getProblem());
        assertEquals(dto.observation(), result.getObservation());
        assertNotNull(result.getId());

        verify(smartphoneRepository).save(any(Smartphone.class));
    }

    @Test
    @DisplayName("Deve atualizar smartphone com sucesso")
    void updateSmartphoneShouldSucceed() {
        SmartphoneUpdateDTO dto = new SmartphoneUpdateDTO("iPhone 13", "Quebrou tela", "Novo", "123456789");

        when(smartphoneRepository.findById("smart-1")).thenReturn(Optional.of(smartphone));
        when(smartphoneRepository.save(any(Smartphone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Smartphone result = smartphoneService.updateSmartphone("smart-1", dto);

        assertEquals(dto.model(), result.getModel());
        assertEquals(dto.problem(), result.getProblem());
        assertEquals(dto.observation(), result.getObservation());

        verify(smartphoneRepository).findById("smart-1");
        verify(smartphoneRepository).save(smartphone);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar smartphone inexistente")
    void updateSmartphoneShouldThrowWhenNotFound() {
        SmartphoneUpdateDTO dto = new SmartphoneUpdateDTO("iPhone 13", "Quebrou tela", "Novo", "123456789");

        when(smartphoneRepository.findById("not-found")).thenReturn(Optional.empty());

        assertThrows(InvalidSmartphoneIdException.class, () -> smartphoneService.updateSmartphone("not-found", dto));
        verify(smartphoneRepository).findById("not-found");
    }

    @Test
    @DisplayName("Deve deletar smartphone com sucesso")
    void deleteSmartphoneShouldSucceed() {
        when(smartphoneRepository.findById("smart-1")).thenReturn(Optional.of(smartphone));
        doNothing().when(smartphoneRepository).delete(smartphone);

        smartphoneService.deleteSmartphone("smart-1");

        verify(smartphoneRepository).findById("smart-1");
        verify(smartphoneRepository).delete(smartphone);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar smartphone inexistente")
    void deleteSmartphoneShouldThrowWhenNotFound() {
        when(smartphoneRepository.findById("not-found")).thenReturn(Optional.empty());

        assertThrows(InvalidSmartphoneIdException.class, () -> smartphoneService.deleteSmartphone("not-found"));
        verify(smartphoneRepository).findById("not-found");
    }

    @Test
    @DisplayName("Deve buscar smartphone por ID com sucesso")
    void getSmartphoneByIdShouldSucceed() {
        when(smartphoneRepository.findById("smart-1")).thenReturn(Optional.of(smartphone));

        Smartphone result = smartphoneService.getSmartphoneById("smart-1");

        assertEquals(smartphone, result);
        verify(smartphoneRepository).findById("smart-1");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar smartphone inexistente")
    void getSmartphoneByIdShouldThrowWhenNotFound() {
        when(smartphoneRepository.findById("not-found")).thenReturn(Optional.empty());

        assertThrows(InvalidSmartphoneIdException.class, () -> smartphoneService.getSmartphoneById("not-found"));
        verify(smartphoneRepository).findById("not-found");
    }
}