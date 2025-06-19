package br.com.tech.os.ostech.controller;

import br.com.tech.os.ostech.exception.InvalidBudgetIdException;
import br.com.tech.os.ostech.infra.security.TokenService;
import br.com.tech.os.ostech.model.Budget;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetCreateDTO;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetUpdateDTO;
import br.com.tech.os.ostech.repository.UserRepository;
import br.com.tech.os.ostech.security.TestSecurityConfig;
import br.com.tech.os.ostech.service.BudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(BudgetController.class)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BudgetService budgetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarBudgetComSucesso() throws Exception {
        BudgetCreateDTO dto = new BudgetCreateDTO(100.0, "desc", "obs", true, "technical-1");
        Budget budget = new Budget();
        budget.setId("budget-1");
        budget.setDescription("desc");
        budget.setValue(100.0);

        Mockito.when(budgetService.createBudget(any(BudgetCreateDTO.class))).thenReturn(budget);

        mockMvc.perform(post("/v1/budget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("budget-1"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.value").value(100.0));
    }

    @Test
    void deveAtualizarBudgetComSucesso() throws Exception {
        BudgetUpdateDTO dto = new BudgetUpdateDTO(200.0, "desc2", "obs2", false, "technical-1");
        Budget budget = new Budget();
        budget.setId("budget-1");
        budget.setDescription("desc2");
        budget.setValue(200.0);

        Mockito.when(budgetService.updateBudget(eq("budget-1"), any(BudgetUpdateDTO.class))).thenReturn(budget);

        mockMvc.perform(put("/v1/budget/budget-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("budget-1"))
                .andExpect(jsonPath("$.description").value("desc2"))
                .andExpect(jsonPath("$.value").value(200.0));
    }

    @Test
    void deveLancar404AoAtualizarBudgetInexistente() throws Exception {
        BudgetUpdateDTO dto = new BudgetUpdateDTO(200.0, "desc2", "obs2", false, "technical-1");

        Mockito.when(budgetService.updateBudget(eq("not-found"), any(BudgetUpdateDTO.class)))
                .thenThrow(new InvalidBudgetIdException("Budget não encontrado"));

        mockMvc.perform(put("/v1/budget/not-found")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Budget não encontrado"));
    }

    @Test
    void deveDeletarBudgetComSucesso() throws Exception {
        Mockito.doNothing().when(budgetService).deleteBudget("budget-1");

        mockMvc.perform(delete("/v1/budget/budget-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveLancar404AoDeletarBudgetInexistente() throws Exception {
        Mockito.doThrow(new InvalidBudgetIdException("Budget não encontrado"))
                .when(budgetService).deleteBudget("not-found");

        mockMvc.perform(delete("/v1/budget/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Budget não encontrado"));
    }

    @Test
    void deveBuscarBudgetPorIdComSucesso() throws Exception {
        Budget budget = new Budget();
        budget.setId("budget-1");
        budget.setDescription("desc");
        budget.setValue(100.0);

        Mockito.when(budgetService.getBudgetById("budget-1")).thenReturn(budget);

        mockMvc.perform(get("/v1/budget/budget-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("budget-1"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.value").value(100.0));
    }

    @Test
    void deveLancar404AoBuscarBudgetInexistente() throws Exception {
        Mockito.when(budgetService.getBudgetById("not-found"))
                .thenThrow(new InvalidBudgetIdException("Budget não encontrado"));

        mockMvc.perform(get("/v1/budget/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Budget não encontrado"));
    }

    @Test
    void deveBuscarTodosBudgetsComSucesso() throws Exception {
        Budget b1 = new Budget();
        b1.setId("1");
        b1.setDescription("desc1");
        b1.setValue(10.0);
        Budget b2 = new Budget();
        b2.setId("2");
        b2.setDescription("desc2");
        b2.setValue(20.0);

        Mockito.when(budgetService.getAllBudgets()).thenReturn(List.of(b1, b2));

        mockMvc.perform(get("/v1/budget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }
}