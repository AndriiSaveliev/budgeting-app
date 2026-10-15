package edu.ivytech.budget.model;

/**
 * Savings: 20% of income.
 * Opposite of Needs and Wants: reaching the limit is the goal, not a warning.
 */
public class SavingsCategory extends BudgetCategory {

    public SavingsCategory() {
        super(CategoryType.SAVINGS);
    }

    @Override
    public boolean isOnTrack() {
        return getTotal() >= getLimit();
    }

    @Override
    public String getStatusMessage() {
        if (isOnTrack()) {
            return "Savings goal reached. Great job!";
        }
        return String.format("Save $%,.2f more to reach your goal.", getRemaining());
    }
}
