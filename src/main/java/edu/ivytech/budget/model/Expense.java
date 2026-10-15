package edu.ivytech.budget.model;

import java.time.LocalDate;

/**
 * One entry in the budget: an expense under Needs or Wants,
 * or a deposit under Savings.
 */
public class Expense {
    private final long id;
    private final String description;
    private final double amount;
    private final CategoryType category;
    private final LocalDate date;

    public Expense(long id, String description, double amount, CategoryType category, LocalDate date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category is required.");
        }
        this.id = id;
        this.description = (description == null || description.isBlank())
                ? "(no description)" : description.trim();
        this.amount = amount;
        this.category = category;
        this.date = (date == null) ? LocalDate.now() : date;
    }

    public long getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public CategoryType getCategory() { return category; }
    public LocalDate getDate() { return date; }
}
