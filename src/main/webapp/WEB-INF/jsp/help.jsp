<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Help | 50/30/20 Budget</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>50/30/20 Budget: Help</h1>
    <nav><a href="${pageContext.request.contextPath}/">Back to the app</a></nav>
</header>
<main>
    <section class="card">
        <h2>What is the 50/30/20 method?</h2>
        <p>Your monthly take home pay is split into three parts:</p>
        <ul>
            <li><strong>Needs, 50%:</strong> things you must pay, like rent, utilities, groceries and insurance.</li>
            <li><strong>Wants, 30%:</strong> things you choose to spend on, like dining out, hobbies and subscriptions.</li>
            <li><strong>Savings, 20%:</strong> money you put away, like an emergency fund or extra debt payments.</li>
        </ul>
    </section>
    <section class="card">
        <h2>How to use the app</h2>
        <ol>
            <li>Sign in with a username (3 to 20 letters, numbers or underscore). A new name creates a new, empty budget.</li>
            <li>Enter your monthly income and click <em>Save income</em>. The app calculates the target for each category.</li>
            <li>Add each expense or savings deposit: choose a category, type a description and an amount, then click <em>Add</em>.</li>
            <li>Watch the three cards. Needs and Wants turn red when you go over the target. Savings turns green when you reach the goal.</li>
            <li>Click <em>Delete</em> next to an entry to remove it.</li>
            <li>Click <em>Sign out</em> when you are done. Your data is saved automatically after every change.</li>
        </ol>
    </section>
    <section class="card">
        <h2>Where is my data saved?</h2>
        <p>Each user has a separate file in the <code>data</code> folder, named after the username (for example <code>data/andrii.txt</code>).
            Signing in with the same username later loads the same budget.</p>
    </section>
    <section class="card">
        <h2>Common problems</h2>
        <ul>
            <li><strong>"Please enter a valid number":</strong> type only digits, for example 1500 or 1500.50.</li>
            <li><strong>"Amount must be greater than zero":</strong> negative and zero amounts are not allowed.</li>
            <li><strong>Username rejected:</strong> use only letters, numbers and underscore, 3 to 20 characters.</li>
        </ul>
    </section>
</main>
</body>
</html>
