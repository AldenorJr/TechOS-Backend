package br.com.tech.os.ostech.service;

import br.com.tech.os.ostech.exception.InvalidBudgetIdException;
import br.com.tech.os.ostech.model.Budget;
import br.com.tech.os.ostech.model.Technical;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetCreateDTO;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetUpdateDTO;
import br.com.tech.os.ostech.repository.BudgetRepository;
import br.com.tech.os.ostech.repository.TechnicalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private TechnicalRepository technicalRepository;

    @InjectMocks
    private BudgetService budgetService;

    private Budget budget;
    private Technical technical;

    @BeforeEach
    void setUp() {
        technical = new Technical();
        technical.setId("tech-1");

        budget = new Budget();
        budget.setId("budget-1");
        budget.setValue(100.0);
        budget.setDescription("desc");
        budget.setObservation("obs");
        budget.setApproved(true);
    }

    @Test
    @DisplayName("Deve criar um orçamento com sucesso")
    void createBudgetShouldSucceed() {
        BudgetCreateDTO dto = new BudgetCreateDTO(100.0, "desc", "obs", true, "tech-1");
        when(technicalRepository.findById("tech-1")).thenReturn(Optional.of(technical));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.createBudget(dto);

        assertEquals(dto.value(), result.getValue());
        assertEquals(dto.description(), result.getDescription());
        assertEquals(dto.observation(), result.getObservation());
        assertEquals(dto.approved(), result.getApproved());
        assertNotNull(result.getId());
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    @DisplayName("Deve atualizar um orçamento com sucesso")
    void updateBudgetShouldSucceed() {
        BudgetUpdateDTO dto = new BudgetUpdateDTO(200.0, "nova desc", "nova obs", false, "tech-1");
        when(budgetRepository.existsById("budget-1")).thenReturn(true);
        when(budgetRepository.findById("budget-1")).thenReturn(Optional.of(budget));
        when(technicalRepository.findById("tech-1")).thenReturn(Optional.of(technical));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.updateBudget("budget-1", dto);

        assertEquals(dto.value(), result.getValue());
        assertEquals(dto.description(), result.getDescription());
        assertEquals(dto.observation(), result.getObservation());
        assertEquals(dto.approved(), result.getApproved());
        verify(budgetRepository).save(budget);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar orçamento inexistente")
    void updateBudgetShouldThrowWhenNotFound() {
        BudgetUpdateDTO dto = new BudgetUpdateDTO(200.0, "nova desc", "nova obs", false, "tech-1");
        when(budgetRepository.existsById("budget-2")).thenReturn(false);

        assertThrows(InvalidBudgetIdException.class, () -> budgetService.updateBudget("budget-2", dto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar orçamento com technicalId inexistente")
    void createBudgetShouldThrowWhenTechnicalNotFound() {
        BudgetCreateDTO dto = new BudgetCreateDTO(100.0, "desc", "obs", true, "tech-invalido");
        when(technicalRepository.findById("tech-invalido")).thenReturn(Optional.empty());

        assertThrows(InvalidBudgetIdException.class, () -> budgetService.createBudget(dto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar orçamento com technicalId inexistente")
    void updateBudgetShouldThrowWhenTechnicalNotFound() {
        BudgetUpdateDTO dto = new BudgetUpdateDTO(200.0, "nova desc", "nova obs", false, "tech-invalido");
        when(budgetRepository.existsById("budget-1")).thenReturn(true);
        when(technicalRepository.findById("tech-invalido")).thenReturn(Optional.empty());

        assertThrows(InvalidBudgetIdException.class, () -> budgetService.updateBudget("budget-1", dto));
    }

    @Test
    @DisplayName("Deve deletar um orçamento com sucesso")
    void deleteBudgetShouldSucceed() {
        when(budgetRepository.existsById("budget-1")).thenReturn(true);
        doNothing().when(budgetRepository).deleteById("budget-1");

        budgetService.deleteBudget("budget-1");

        verify(budgetRepository).deleteById("budget-1");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar orçamento inexistente")
    void deleteBudgetShouldThrowWhenNotFound() {
        when(budgetRepository.existsById("budget-2")).thenReturn(false);

        assertThrows(InvalidBudgetIdException.class, () -> budgetService.deleteBudget("budget-2"));
        verify(budgetRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve buscar orçamento por ID com sucesso")
    void getBudgetByIdShouldSucceed() {
        when(budgetRepository.findById("budget-1")).thenReturn(Optional.of(budget));

        Budget result = budgetService.getBudgetById("budget-1");

        assertEquals(budget, result);
        verify(budgetRepository).findById("budget-1");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar orçamento inexistente por ID")
    void getBudgetByIdShouldThrowWhenNotFound() {
        when(budgetRepository.findById("budget-2")).thenReturn(Optional.empty());

        assertThrows(InvalidBudgetIdException.class, () -> budgetService.getBudgetById("budget-2"));
    }

    @Test
    @DisplayName("Deve buscar todos os orçamentos")
    void getAllBudgetsShouldReturnList() {
        when(budgetRepository.findAll()).thenReturn(List.of(budget));

        List<Budget> result = budgetService.getAllBudgets();

        assertEquals(1, result.size());
        verify(budgetRepository).findAll();
    }
}