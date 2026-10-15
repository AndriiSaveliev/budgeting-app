<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign In | 50/30/20 Budget</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main>
    <div class="card login">
        <h2>50/30/20 Budgeting App</h2>
        <p class="muted">Enter a username to open your budget. A new username creates a new budget.
            Each user's data is saved separately.</p>

        <c:if test="${not empty error}">
            <div class="error"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login" class="inline">
            <label>Username
                <input type="text" name="username" required minlength="3" maxlength="20"
                       pattern="[A-Za-z0-9_]+" placeholder="for example andrii">
            </label>
            <button type="submit">Open budget</button>
        </form>
        <p class="muted" style="margin-top:16px">New here? Read the <a href="${pageContext.request.contextPath}/help">Help page</a>.</p>
    </div>
</main>
</body>
</html>
