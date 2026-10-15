package edu.ivytech.budget.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for a 50/30/20 category.
 * Holds the logic every category shares (limit, total, remaining).
 * Each subclass decides how to judge its own status (polymorphism).
 */
public abstract class BudgetCategory {
    private final CategoryType type;
    private double monthlyIncome;
    private final List<Expense> expenses = new ArrayList<>();

    protected BudgetCategory(CategoryType type) {
        this.type = type;
    }

    public CategoryType getType() { return type; }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    /** The target amount for this category: income times its percentage. */
    public double getLimit() {
        return monthlyIncome * type.getPercentage();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public boolean removeExpense(long id) {
        return expenses.removeIf(e -> e.getId() == id);
    }

    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    public double getTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    public double getRemaining() {
        return getLimit() - getTotal();
    }

    /** How much of the limit is used, from 0 to 100, for the progress bar. */
    public int getPercentUsed() {
        if (getLimit() <= 0) return 0;
        return (int) Math.min(100, Math.round(getTotal() / getLimit() * 100));
    }

    /** True when the category is in a good state. Each subclass decides what "good" means. */
    public abstract boolean isOnTrack();

    /** Message shown to the user about this category. */
    public abstract String getStatusMessage();
}
