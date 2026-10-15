package edu.ivytech.budget.storage;

import edu.ivytech.budget.model.Budget;
import edu.ivytech.budget.model.CategoryType;
import edu.ivytech.budget.model.Expense;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves each user's budget in its own text file: data/{username}.txt
 *
 * File format:
 *   income=4000.0
 *   expense|1|2026-10-15|NEEDS|1500.0|Rent
 *
 * The description is the last field, so it may safely contain the | character.
 */
@Repository
public class FileBudgetRepository implements BudgetRepository {

    private final Path dataDir;

    public FileBudgetRepository(@Value("${budget.data-dir:data}") String dataDir) {
        this.dataDir = Paths.get(dataDir);
    }

    private Path fileFor(String username) {
        // Usernames are validated before they get here, so this cannot leave the data folder.
        return dataDir.resolve(username + ".txt");
    }

    @Override
    public Budget load(String username) {
        Budget budget = new Budget(username);
        Path file = fileFor(username);
        if (!Files.exists(file)) {
            return budget;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.startsWith("income=")) {
                    budget.setMonthlyIncome(Double.parseDouble(line.substring(7)));
                } else if (line.startsWith("expense|")) {
                    String[] p = line.split("\\|", 6);
                    budget.restoreExpense(new Expense(
                            Long.parseLong(p[1]),
                            p[5],
                            Double.parseDouble(p[4]),
                            CategoryType.valueOf(p[3]),
                            LocalDate.parse(p[2])));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read budget for " + username, e);
        }
        return budget;
    }

    @Override
    public void save(Budget budget) {
        List<String> lines = new ArrayList<>();
        lines.add("income=" + budget.getMonthlyIncome());
        for (Expense e : budget.getAllExpenses()) {
            String desc = e.getDescription().replace("\r", " ").replace("\n", " ");
            lines.add("expense|" + e.getId() + "|" + e.getDate() + "|"
                    + e.getCategory().name() + "|" + e.getAmount() + "|" + desc);
        }
        try {
            Files.createDirectories(dataDir);
            Files.write(fileFor(budget.getUsername()), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save budget for " + budget.getUsername(), e);
        }
    }
}
