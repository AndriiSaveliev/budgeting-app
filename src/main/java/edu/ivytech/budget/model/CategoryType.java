package edu.ivytech.budget.model;

/**
 * The three categories of the 50/30/20 budgeting method.
 * Each constant stores its display name, its share of income,
 * and a short hint shown to the user on the budget page.
 */
public enum CategoryType {
    NEEDS("Needs", 0.50, "Rent, utilities, groceries, insurance, minimum loan payments"),
    WANTS("Wants", 0.30, "Dining out, entertainment, hobbies, subscriptions"),
    SAVINGS("Savings", 0.20, "Emergency fund, retirement, extra debt payments");

    private final String displayName;
    private final double percentage;
    private final String examples;

    CategoryType(String displayName, double percentage, String examples) {
        this.displayName = displayName;
        this.percentage = percentage;
        this.examples = examples;
    }

    public String getDisplayName() { return displayName; }

    /** Share of monthly income as a decimal, for example 0.50. */
    public double getPercentage() { return percentage; }

    /** Share of monthly income as a whole number, for example 50. */
    public int getPercent() { return (int) Math.round(percentage * 100); }

    public String getExamples() { return examples; }

    /** Needed by JSP, because EL cannot call name() on an enum directly. */
    public String getName() { return name(); }
}
