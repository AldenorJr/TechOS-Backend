package br.com.tech.os.ostech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.tech.os.ostech.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, String> {
}
