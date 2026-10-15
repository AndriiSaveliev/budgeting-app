package edu.ivytech.budget.service;

import edu.ivytech.budget.model.Budget;
import edu.ivytech.budget.model.CategoryType;
import edu.ivytech.budget.storage.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Business logic between the web pages and storage.
 * Every change is saved right away, so nothing is lost if the app closes.
 */
@Service
public class BudgetService {

    /** 3 to 20 letters, digits or underscores. Keeps file names safe. */
    private static final Pattern VALID_USERNAME = Pattern.compile("[A-Za-z0-9_]{3,20}");

    private final BudgetRepository repository;

    public BudgetService(BudgetRepository repository) {
        this.repository = repository;
    }

    /** Returns the cleaned username, or throws if it is not allowed. */
    public String normalizeUsername(String raw) {
        String name = (raw == null) ? "" : raw.trim().toLowerCase();
        if (!VALID_USERNAME.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    "Username must be 3 to 20 characters: letters, numbers or underscore.");
        }
        return name;
    }

    public Budget getBudget(String username) {
        return repository.load(username);
    }

    public void setIncome(String username, double income) {
        if (income <= 0) {
            throw new IllegalArgumentException("Income must be greater than zero.");
        }
        Budget budget = repository.load(username);
        budget.setMonthlyIncome(income);
        repository.save(budget);
    }

    public void addExpense(String username, String description, double amount, CategoryType type) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        Budget budget = repository.load(username);
        budget.addExpense(description, amount, type);
        repository.save(budget);
    }

    public void deleteExpense(String username, long id) {
        Budget budget = repository.load(username);
        if (budget.removeExpense(id)) {
            repository.save(budget);
        }
    }
}
