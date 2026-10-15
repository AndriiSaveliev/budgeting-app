package edu.ivytech.budget;

import edu.ivytech.budget.model.Budget;
import edu.ivytech.budget.model.CategoryType;
import edu.ivytech.budget.storage.FileBudgetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class BudgetTest {

    @Test
    void limitsFollow503020() {
        Budget b = new Budget("test");
        b.setMonthlyIncome(4000);
        assertEquals(2000, b.getCategory(CategoryType.NEEDS).getLimit(), 0.001);
        assertEquals(1200, b.getCategory(CategoryType.WANTS).getLimit(), 0.001);
        assertEquals(800, b.getCategory(CategoryType.SAVINGS).getLimit(), 0.001);
    }

    @Test
    void needsGoesOffTrackWhenOverLimit() {
        Budget b = new Budget("test");
        b.setMonthlyIncome(1000);
        b.addExpense("Rent", 600, CategoryType.NEEDS);
        assertFalse(b.getCategory(CategoryType.NEEDS).isOnTrack());
    }

    @Test
    void savingsIsOnTrackOnlyWhenGoalReached() {
        Budget b = new Budget("test");
        b.setMonthlyIncome(1000);
        b.addExpense("Fund", 100, CategoryType.SAVINGS);
        assertFalse(b.getCategory(CategoryType.SAVINGS).isOnTrack());
        b.addExpense("Fund", 100, CategoryType.SAVINGS);
        assertTrue(b.getCategory(CategoryType.SAVINGS).isOnTrack());
    }

    @Test
    void negativeAmountIsRejected() {
        Budget b = new Budget("test");
        assertThrows(IllegalArgumentException.class, () -> b.addExpense("Bad", -5, CategoryType.WANTS));
    }

    @Test
    void eachUserIsSavedToOwnFile(@TempDir Path dir) {
        FileBudgetRepository repo = new FileBudgetRepository(dir.toString());

        Budget alice = new Budget("alice");
        alice.setMonthlyIncome(3000);
        alice.addExpense("Rent | apartment", 1200, CategoryType.NEEDS);
        repo.save(alice);

        Budget bob = new Budget("bob");
        bob.setMonthlyIncome(5000);
        repo.save(bob);

        assertTrue(Files.exists(dir.resolve("alice.txt")));
        assertTrue(Files.exists(dir.resolve("bob.txt")));

        Budget loaded = repo.load("alice");
        assertEquals(3000, loaded.getMonthlyIncome(), 0.001);
        assertEquals(1, loaded.getAllExpenses().size());
        assertEquals("Rent | apartment", loaded.getAllExpenses().get(0).getDescription());
        assertEquals(0, repo.load("bob").getAllExpenses().size());
    }
}
