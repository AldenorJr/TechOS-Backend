package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.exception.InvalidNameTechnicalAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidTechnicalIdException;
import br.com.tech.os.ostech.model.Technical;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalCreateDTO;
import br.com.tech.os.ostech.model.dto.technicalDTO.TechnicalUpdateDTO;
import br.com.tech.os.ostech.repository.TechnicalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TechnicalServiceTest {

    @Mock
    private TechnicalRepository technicalRepository;

    @InjectMocks
    private TechnicalService technicalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar técnico com sucesso")
    void createTechnical_shouldCreateTechnical() {
        TechnicalCreateDTO dto = new TechnicalCreateDTO("Novo Técnico");
        when(technicalRepository.existsByName(dto.name())).thenReturn(false);
        when(technicalRepository.save(any(Technical.class))).thenAnswer(i -> i.getArgument(0));

        Technical result = technicalService.createTechnical(dto);

        assertEquals(dto.name(), result.getName());
        verify(technicalRepository).save(any(Technical.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar técnico com nome já existente")
    void createTechnical_shouldThrowIfNameExists() {
        TechnicalCreateDTO dto = new TechnicalCreateDTO("Existente");
        when(technicalRepository.existsByName(dto.name())).thenReturn(true);

        assertThrows(InvalidNameTechnicalAlreadyExistsException.class, () -> technicalService.createTechnical(dto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar técnico com ID inválido")
    void updateTechnical_shouldThrowIfIdNotFound() {
        String id = "1";
        TechnicalUpdateDTO dto = new TechnicalUpdateDTO("Novo Nome");
        when(technicalRepository.existsById(id)).thenReturn(false);

        assertThrows(InvalidTechnicalIdException.class, () -> technicalService.updateTechnical(id, dto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar técnico com nome já existente")
    void updateTechnical_shouldThrowIfNameExists() {
        String id = "1";
        TechnicalUpdateDTO dto = new TechnicalUpdateDTO("Nome Existente");
        when(technicalRepository.existsById(id)).thenReturn(true);
        when(technicalRepository.existsByName(dto.name())).thenReturn(true);

        assertThrows(InvalidNameTechnicalAlreadyExistsException.class, () -> technicalService.updateTechnical(id, dto));
    }

    @Test
    @DisplayName("Deve atualizar técnico com sucesso")
    void updateTechnical_shouldUpdateTechnical() {
        String id = "1";

        TechnicalUpdateDTO dto = new TechnicalUpdateDTO("Nome Atualizado");

        Technical technical = new Technical();
        technical.setId(id);
        technical.setName("Antigo Nome");

        when(technicalRepository.existsById(id)).thenReturn(true);
        when(technicalRepository.existsByName(dto.name())).thenReturn(false);
        when(technicalRepository.findById(id)).thenReturn(Optional.of(technical));
        when(technicalRepository.save(any(Technical.class))).thenAnswer(i -> i.getArgument(0));

        Technical result = technicalService.updateTechnical(id, dto);

        assertEquals(dto.name(), result.getName());
        verify(technicalRepository).save(any(Technical.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar técnico que não existe")
    void deleteTechnical_shouldThrowIfIdNotFound() {
        String id = "1";
        when(technicalRepository.existsById(id)).thenReturn(false);

        assertThrows(InvalidTechnicalIdException.class, () -> technicalService.deleteTechnical(id));
    }

    @Test
    @DisplayName("Deve deletar técnico com sucesso")
    void deleteTechnical_shouldDelete() {
        String id = "1";
        when(technicalRepository.existsById(id)).thenReturn(true);

        technicalService.deleteTechnical(id);

        verify(technicalRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve buscar técnico por ID com sucesso")
    void getTechnicalById_shouldReturnTechnical() {
        String id = "1";
        Technical technical = new Technical();
        technical.setId(id);
        when(technicalRepository.findById(id)).thenReturn(Optional.of(technical));

        Technical result = technicalService.getTechnicalById(id);

        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar técnico por ID que não existe")
    void getTechnicalById_shouldThrowIfNotFound() {
        String id = "1";
        when(technicalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidTechnicalIdException.class, () -> technicalService.getTechnicalById(id));
    }

    @Test
    @DisplayName("Deve retornar uma lista de técnicos")
    void getAllTechnicals_shouldReturnList() {
        List<Technical> list = Arrays.asList(new Technical(), new Technical());
        when(technicalRepository.findAll()).thenReturn(list);

        List<Technical> result = technicalService.getAllTechnicals();

        assertEquals(2, result.size());
    }

}