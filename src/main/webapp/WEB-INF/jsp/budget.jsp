<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="en_US"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Budget | 50/30/20 Budget</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<header>
    <h1>50/30/20 Budget</h1>
    <nav>
        <span class="muted">Signed in as <strong><c:out value="${budget.username}"/></strong></span>
        <a href="${ctx}/help">Help</a>
        <form method="post" action="${ctx}/logout"><button class="link" type="submit">Sign out</button></form>
    </nav>
</header>

<main>
    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <!-- Monthly income -->
    <section class="card">
        <h2>Monthly income</h2>
        <form method="post" action="${ctx}/income" class="inline">
            <label>Take home pay per month ($)
                <input type="text" name="income" required inputmode="decimal"
                       value="<c:if test='${budget.monthlyIncome > 0}'><fmt:formatNumber value='${budget.monthlyIncome}' pattern='0.00'/></c:if>">
            </label>
            <button type="submit">Save income</button>
        </form>
        <c:if test="${budget.monthlyIncome > 0}">
            <p class="muted" style="margin-bottom:0">
                Recorded so far: <fmt:formatNumber value="${budget.totalSpent}" type="currency"/>
                &nbsp;|&nbsp; Not yet assigned: <fmt:formatNumber value="${budget.unassigned}" type="currency"/>
            </p>
        </c:if>
    </section>

    <c:choose>
        <c:when test="${budget.monthlyIncome <= 0}">
            <section class="card">
                <p>Enter your monthly income above to see your 50/30/20 limits.</p>
            </section>
        </c:when>
        <c:otherwise>
            <!-- One card per category -->
            <section class="grid" style="margin-bottom:20px">
                <c:forEach var="cat" items="${budget.categories}">
                    <div class="card cat ${cat.type.name}" style="margin-bottom:0">
                        <h2><c:out value="${cat.type.displayName}"/> (${cat.type.percent}%)</h2>
                        <p class="hint"><c:out value="${cat.type.examples}"/></p>
                        <div class="row"><span>Target</span><span><fmt:formatNumber value="${cat.limit}" type="currency"/></span></div>
                        <div class="row"><span>${cat.type.name == 'SAVINGS' ? 'Saved' : 'Spent'}</span><span><fmt:formatNumber value="${cat.total}" type="currency"/></span></div>
                        <div class="row"><span>Remaining</span><span><fmt:formatNumber value="${cat.remaining}" type="currency"/></span></div>
                        <div class="bar"><span style="width:${cat.percentUsed}%"></span></div>
                        <div class="status ${cat.onTrack ? 'ok' : 'warn'}"><c:out value="${cat.statusMessage}"/></div>
                    </div>
                </c:forEach>
            </section>

            <!-- Add an entry -->
            <section class="card">
                <h2>Add an expense or savings deposit</h2>
                <form method="post" action="${ctx}/expense" class="inline">
                    <label>Category
                        <select name="category">
                            <c:forEach var="t" items="${categoryTypes}">
                                <option value="${t.name}"><c:out value="${t.displayName}"/> (${t.percent}%)</option>
                            </c:forEach>
                        </select>
                    </label>
                    <label>Description
                        <input type="text" name="description" maxlength="60" placeholder="for example Rent">
                    </label>
                    <label>Amount ($)
                        <input type="text" name="amount" required inputmode="decimal" placeholder="0.00">
                    </label>
                    <button type="submit">Add</button>
                </form>
            </section>

            <!-- List of entries -->
            <section class="card">
                <h2>Entries</h2>
                <c:choose>
                    <c:when test="${empty budget.allExpenses}">
                        <p class="muted">No entries yet.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-wrap">
                            <table>
                                <thead>
                                <tr><th>Date</th><th>Category</th><th>Description</th><th class="num">Amount</th><th></th></tr>
                                </thead>
                                <tbody>
                                <c:forEach var="e" items="${budget.allExpenses}">
                                    <tr>
                                        <td><c:out value="${e.date}"/></td>
                                        <td><span class="pill ${e.category.name}"><c:out value="${e.category.displayName}"/></span></td>
                                        <td><c:out value="${e.description}"/></td>
                                        <td class="num"><fmt:formatNumber value="${e.amount}" type="currency"/></td>
                                        <td class="num">
                                            <form method="post" action="${ctx}/expense/delete">
                                                <input type="hidden" name="id" value="${e.id}">
                                                <button class="danger" type="submit" title="Delete this entry">Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
