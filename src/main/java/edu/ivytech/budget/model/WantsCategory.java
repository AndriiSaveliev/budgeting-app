package edu.ivytech.budget.model;

/** Wants: 30% of income. Staying under the limit is the goal. */
public class WantsCategory extends BudgetCategory {

    public WantsCategory() {
        super(CategoryType.WANTS);
    }

    @Override
    public boolean isOnTrack() {
        return getRemaining() >= 0;
    }

    @Override
    public String getStatusMessage() {
        if (!isOnTrack()) {
            return String.format("Over budget by $%,.2f. Consider cutting back on extras.", -getRemaining());
        }
        return "Within budget.";
    }
}
