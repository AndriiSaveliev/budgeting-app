package edu.ivytech.budget.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * One user's budget: monthly income plus the three categories.
 */
public class Budget {
    private final String username;
    private double monthlyIncome;
    private long nextExpenseId = 1;
    private final Map<CategoryType, BudgetCategory> categories = new EnumMap<>(CategoryType.class);

    public Budget(String username) {
        this.username = username;
        categories.put(CategoryType.NEEDS, new NeedsCategory());
        categories.put(CategoryType.WANTS, new WantsCategory());
        categories.put(CategoryType.SAVINGS, new SavingsCategory());
    }

    public String getUsername() { return username; }

    public double getMonthlyIncome() { return monthlyIncome; }

    /** Income as plain text for the input box, always with a dot, for example 4000.00 */
    public String getIncomeForInput() {
        return String.format(java.util.Locale.US, "%.2f", monthlyIncome);
    }

    public void setMonthlyIncome(double income) {
        if (income < 0) {
            throw new IllegalArgumentException("Income cannot be negative.");
        }
        this.monthlyIncome = income;
        for (BudgetCategory c : categories.values()) {
            c.setMonthlyIncome(income);
        }
    }

    public List<BudgetCategory> getCategories() {
        return new ArrayList<>(categories.values());
    }

    public BudgetCategory getCategory(CategoryType type) {
        return categories.get(type);
    }

    /** Creates a new entry with the next id and puts it in the right category. */
    public Expense addExpense(String description, double amount, CategoryType type) {
        Expense e = new Expense(nextExpenseId++, description, amount, type, null);
        categories.get(type).addExpense(e);
        return e;
    }

    /** Used when loading saved data, so the original id and date are kept. */
    public void restoreExpense(Expense e) {
        categories.get(e.getCategory()).addExpense(e);
        if (e.getId() >= nextExpenseId) {
            nextExpenseId = e.getId() + 1;
        }
    }

    public boolean removeExpense(long id) {
        for (BudgetCategory c : categories.values()) {
            if (c.removeExpense(id)) return true;
        }
        return false;
    }

    /** All entries from every category, newest first. */
    public List<Expense> getAllExpenses() {
        List<Expense> all = new ArrayList<>();
        for (BudgetCategory c : categories.values()) {
            all.addAll(c.getExpenses());
        }
        all.sort(Comparator.comparing(Expense::getDate).thenComparing(Expense::getId).reversed());
        return all;
    }

    public double getTotalSpent() {
        double total = 0;
        for (BudgetCategory c : categories.values()) {
            total += c.getTotal();
        }
        return total;
    }

    public double getUnassigned() {
        return monthlyIncome - getTotalSpent();
    }
}
