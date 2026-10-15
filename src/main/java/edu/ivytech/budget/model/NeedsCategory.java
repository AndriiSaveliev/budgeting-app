package edu.ivytech.budget.model;

/** Needs: 50% of income. Staying under the limit is the goal. */
public class NeedsCategory extends BudgetCategory {

    public NeedsCategory() {
        super(CategoryType.NEEDS);
    }

    @Override
    public boolean isOnTrack() {
        return getRemaining() >= 0;
    }

    @Override
    public String getStatusMessage() {
        if (!isOnTrack()) {
            return String.format("Over budget by $%,.2f. Look for ways to lower fixed costs.", -getRemaining());
        }
        return "Within budget.";
    }
}
