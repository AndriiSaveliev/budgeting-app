package edu.ivytech.budget.web;

import edu.ivytech.budget.model.CategoryType;
import edu.ivytech.budget.service.BudgetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles the web pages. Each method returns the name of a JSP page
 * (in WEB-INF/jsp) or redirects to another URL.
 */
@Controller
public class BudgetController {

    private static final String USER_KEY = "username";

    private final BudgetService service;

    public BudgetController(BudgetService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        return session.getAttribute(USER_KEY) == null ? "redirect:/login" : "redirect:/budget";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, HttpSession session, Model model) {
        try {
            session.setAttribute(USER_KEY, service.normalizeUsername(username));
            return "redirect:/budget";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/budget")
    public String budgetPage(HttpSession session, Model model) {
        String user = (String) session.getAttribute(USER_KEY);
        if (user == null) return "redirect:/login";
        model.addAttribute("budget", service.getBudget(user));
        model.addAttribute("categoryTypes", CategoryType.values());
        return "budget";
    }

    @PostMapping("/income")
    public String setIncome(@RequestParam("income") String income, HttpSession session, RedirectAttributes flash) {
        String user = (String) session.getAttribute(USER_KEY);
        if (user == null) return "redirect:/login";
        try {
            service.setIncome(user, parseMoney(income));
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/budget";
    }

    @PostMapping("/expense")
    public String addExpense(@RequestParam("description") String description, @RequestParam("amount") String amount,
                             @RequestParam("category") CategoryType category,
                             HttpSession session, RedirectAttributes flash) {
        String user = (String) session.getAttribute(USER_KEY);
        if (user == null) return "redirect:/login";
        try {
            service.addExpense(user, description, parseMoney(amount), category);
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/budget";
    }

    @PostMapping("/expense/delete")
    public String deleteExpense(@RequestParam("id") long id, HttpSession session) {
        String user = (String) session.getAttribute(USER_KEY);
        if (user == null) return "redirect:/login";
        service.deleteExpense(user, id);
        return "redirect:/budget";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }

    /**
     * Accepts "1500", "1500.50", "1,500.00", "$1500" and also "1500,50"
     * (comma as decimal point). Throws a friendly message for anything else.
     */
    static double parseMoney(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
        String s = text.replace("$", "").replace(" ", "").trim();
        if (s.matches("\\d+,\\d{1,2}")) {
            s = s.replace(",", ".");      // 1500,50 means 1500.50
        } else {
            s = s.replace(",", "");       // 1,500.00 means 1500.00
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }
}
