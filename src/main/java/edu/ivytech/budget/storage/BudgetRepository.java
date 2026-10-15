package edu.ivytech.budget.storage;

import edu.ivytech.budget.model.Budget;

/**
 * Describes how budgets are saved and loaded.
 * The rest of the app depends only on this interface, so the storage
 * could be swapped (for example to a database) without changing other code.
 */
public interface BudgetRepository {

    /** Loads the budget for this user, or returns a new empty budget if none exists. */
    Budget load(String username);

    /** Saves the budget to its own user's storage. */
    void save(Budget budget);
}
