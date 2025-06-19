package br.com.tech.os.ostech.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.tech.os.ostech.model.Budget;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetCreateDTO;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetUpdateDTO;
import br.com.tech.os.ostech.service.BudgetService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping()
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetCreateDTO budgetCreateDTO) {
        Budget budget = budgetService.createBudget(budgetCreateDTO);
        return ResponseEntity.ok(budget);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable String id, @RequestBody BudgetUpdateDTO budgetCreateDTO) {
        Budget budget = budgetService.updateBudget(id, budgetCreateDTO);
        return ResponseEntity.ok(budget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable String id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getClientById(@PathVariable String id) {
        Budget budget = budgetService.getBudgetById(id);
        return ResponseEntity.ok(budget);
    }

    @GetMapping()
    public ResponseEntity<List<Budget>> getAllBudgets() {
        List<Budget> budget = budgetService.getAllBudgets();
        return ResponseEntity.ok(budget);
    }

}
