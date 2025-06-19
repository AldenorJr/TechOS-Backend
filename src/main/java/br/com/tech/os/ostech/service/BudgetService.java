package br.com.tech.os.ostech.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.tech.os.ostech.model.Technical;
import br.com.tech.os.ostech.repository.TechnicalRepository;
import org.springframework.stereotype.Service;
import br.com.tech.os.ostech.exception.InvalidBudgetIdException;
import br.com.tech.os.ostech.model.Budget;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetCreateDTO;
import br.com.tech.os.ostech.model.dto.budgetDTO.BudgetUpdateDTO;
import br.com.tech.os.ostech.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TechnicalRepository technicalRepository;

    public Budget createBudget(BudgetCreateDTO budgetCreateDTO) {
        Budget budget = new Budget();

        Optional<Technical> technicalOptional = technicalRepository.findById(budgetCreateDTO.technicalId());
        if (technicalOptional.isEmpty()) {
            log.error("Technical with id {} not found", budgetCreateDTO.technicalId());
            throw new InvalidBudgetIdException("Technical not found");
        }
        Technical technical = technicalOptional.get();

        budget.setTechnical(technical);
        budget.setValue(budgetCreateDTO.value());
        budget.setDescription(budgetCreateDTO.description());
        budget.setObservation(budgetCreateDTO.observation());
        budget.setApproved(budgetCreateDTO.approved());
        budget.setId(UUID.randomUUID().toString());

        log.info("Creating budget with value: {} and description: {}", budget.getValue(), budget.getDescription());
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(String id, BudgetUpdateDTO budgetUpdateDTO) {
        if (!budgetRepository.existsById(id)) {
            log.error("Budget with id {} not found", id);
            throw new InvalidBudgetIdException("Budget not found");
        }

        Optional<Technical> technicalOptional = technicalRepository.findById(budgetUpdateDTO.technicalId());
        if (technicalOptional.isEmpty()) {
            log.error("Technical with id {} not found", budgetUpdateDTO.technicalId());
            throw new InvalidBudgetIdException("Technical not found");
        }
        Technical technical = technicalOptional.get();

        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new InvalidBudgetIdException("Budget not found"));
        budget.setTechnical(technical);
        budget.setValue(budgetUpdateDTO.value());
        budget.setDescription(budgetUpdateDTO.description());
        budget.setObservation(budgetUpdateDTO.observation());
        budget.setApproved(budgetUpdateDTO.approved());
        log.info("Updating budget {}", budget);

        return budgetRepository.save(budget);
    }

    public void deleteBudget(String id) {
        if (!budgetRepository.existsById(id)) {
            log.error("Budget with id {} not found", id);
            throw new InvalidBudgetIdException("Budget not found");
        }
        log.info("Deleting budget with id {}", id);
        budgetRepository.deleteById(id);
    }

    public Budget getBudgetById(String id) {
        log.info("Getting budget with id {}", id);
        return budgetRepository.findById(id).orElseThrow(() -> new InvalidBudgetIdException("Budget not found"));
    }

    public List<Budget> getAllBudgets() {
        log.info("Getting all budgets");
        return budgetRepository.findAll();
    }

}
