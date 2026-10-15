# 50/30/20 Budgeting App

A Java web application that helps a person manage a monthly budget with the **50/30/20 method**.
Built with Spring Boot, with JSP pages as the graphical user interface.

Author: Andrii Saveliev, Ivy Tech Community College, Fall 2026

---

## Contents

1. [What the app does](#what-the-app-does)
2. [Requirements](#requirements)
3. [How to run](#how-to-run)
4. [User guide](#user-guide)
5. [How data is saved](#how-data-is-saved)
6. [Project structure](#project-structure)
7. [Class overview](#class-overview)
8. [Troubleshooting](#troubleshooting)

---

## What the app does

The 50/30/20 method splits monthly take home pay into three parts:

| Category | Share | Examples |
|---|---|---|
| Needs | 50% | Rent, utilities, groceries, insurance |
| Wants | 30% | Dining out, entertainment, hobbies |
| Savings | 20% | Emergency fund, retirement, extra debt payments |

The app lets each user:

* sign in with a username (each user has separate, saved data)
* enter a monthly income and see the target for each category
* add expenses or savings deposits to a category
* see how much is spent, how much is left, and a status message for each category
* delete entries
* read a built in Help page

## Requirements

* Java JDK 17 or newer
* Maven 3.8 or newer (IntelliJ IDEA and most Java IDEs include Maven)
* Internet connection the first time, so Maven can download the libraries

## How to run

**Option 1: IntelliJ IDEA**

1. Open the project folder (the one with `pom.xml`). IntelliJ imports it as a Maven project.
2. Wait for the dependencies to download.
3. Open `src/main/java/edu/ivytech/budget/BudgetingApplication.java` and click the green Run button.
4. Open **http://localhost:8080** in a browser.

**Option 2: command line**

```bash
mvn spring-boot:run
```

Then open **http://localhost:8080**.

**Option 3: build and run the packaged file**

```bash
mvn package
java -jar target/budgeting-app-1.0.0.war
```

To stop the app, press `Ctrl + C` in the terminal or click Stop in the IDE.

**Run the tests**

```bash
mvn test
```

## User guide

1. **Sign in.** Type a username (3 to 20 letters, numbers or underscore) and click **Open budget**.
   A new username creates a new empty budget. An existing username opens that user's saved budget.
2. **Set income.** Enter your monthly take home pay and click **Save income**.
   Three cards appear with the target for Needs, Wants and Savings.
3. **Add entries.** Choose a category, type a description (optional) and an amount, then click **Add**.
   Amounts like `1500`, `1500.50`, `1,500` and `$1500` are accepted.
4. **Read the cards.**
   * Needs and Wants show a green "Within budget" message, or a red message when you go over the target.
   * Savings works the other way: it shows how much more to save, and turns green when the goal is reached.
   * The bar shows how much of the target is used.
5. **Delete an entry.** Click **Delete** next to it in the Entries table.
6. **Sign out.** Click **Sign out** in the top right corner. Your data is already saved.

A short version of this guide is also inside the app, on the **Help** page (`/help`).

## How data is saved

Each user's budget is saved in a separate text file in the `data` folder:

```
data/andrii.txt
data/maria.txt
```

File format:

```
income=4000.0
expense|1|2026-10-15|NEEDS|1500.0|Rent
expense|2|2026-10-15|WANTS|50.0|Movies
```

The file is written after every change, so nothing is lost when the app is closed.
Usernames are checked (letters, numbers, underscore only), so one user can never read or overwrite another user's file.
The folder can be changed in `src/main/resources/application.properties` (`budget.data-dir`).

## Project structure

```
src/main/java/edu/ivytech/budget/
    BudgetingApplication.java      starts the app
    model/
        CategoryType.java          enum: NEEDS, WANTS, SAVINGS
        Expense.java               one entry
        BudgetCategory.java        abstract class for a category
        NeedsCategory.java         50% category
        WantsCategory.java         30% category
        SavingsCategory.java       20% category
        Budget.java                one user's income and categories
    storage/
        BudgetRepository.java      interface: load and save
        FileBudgetRepository.java  saves each user to data/{username}.txt
    service/
        BudgetService.java         business rules and validation
    web/
        BudgetController.java      connects URLs to JSP pages
src/main/webapp/
    WEB-INF/jsp/login.jsp          sign in page
    WEB-INF/jsp/budget.jsp         main budget page
    WEB-INF/jsp/help.jsp           help page
    css/style.css                  styles
src/test/java/edu/ivytech/budget/
    BudgetTest.java                unit tests
```

## Class overview

| Class | Type | Purpose |
|---|---|---|
| `CategoryType` | **Enum** | The three categories, each with its name, percentage and examples |
| `BudgetCategory` | **Abstract class** | Shared logic: limit, total, remaining. Declares `isOnTrack()` and `getStatusMessage()` |
| `NeedsCategory`, `WantsCategory`, `SavingsCategory` | Subclasses | Each overrides `isOnTrack()` and `getStatusMessage()` (polymorphism). Savings uses the opposite rule |
| `Expense` | Class | One entry: id, description, amount, category, date |
| `Budget` | Class | One user's income and the three categories |
| `BudgetRepository` | **Interface** | Defines `load` and `save`, so storage could be replaced later |
| `FileBudgetRepository` | Class | Implements the interface with one text file per user |
| `BudgetService` | Class | Validates input and saves after each change |
| `BudgetController` | Class | Handles web requests and chooses which JSP page to show |
| `BudgetingApplication` | Class | Main class that starts Spring Boot |

## Troubleshooting

| Problem | Solution |
|---|---|
| `Port 8080 was already in use` | Stop the other running copy, or change `server.port` in `application.properties` |
| Page shows JSP source or a 404 error | Run the app from the project root with `mvn spring-boot:run`, so the `src/main/webapp` folder is found |
| "Please enter a valid number" | Type digits only, for example `1500` or `1500.50` |
| "Amount must be greater than zero" | Zero and negative amounts are not allowed |
| Username rejected | Use 3 to 20 letters, numbers or underscore |
| Dependencies do not download | Check the internet connection, then in IntelliJ use Maven, Reload project |
