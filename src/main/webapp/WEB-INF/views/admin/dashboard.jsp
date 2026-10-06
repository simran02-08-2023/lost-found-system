<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Lost &amp; Found</title>
    <style>
        .cards { display: flex; flex-wrap: wrap; gap: 12px; margin: 16px 0; }
        .card  { border: 1px solid #dfe5e3; border-radius: 8px; padding: 12px 18px; min-width: 150px; background: #f7faf9; }
        .card .num { font-size: 2rem; font-weight: bold; }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jspf" %>
    <h1>Admin dashboard</h1>
    <p>Welcome, <c:out value="${sessionScope.userName}"/>. Only admins can see this page.</p>

    <p><a href="${pageContext.request.contextPath}/admin/claims">Review pending claims</a></p>

    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <c:if test="${not empty stats}">
        <h2>Statistics</h2>
        <div class="cards">
            <div class="card"><div class="num"><c:out value="${stats.users}"/></div>Total users</div>
            <div class="card"><div class="num"><c:out value="${stats.lost}"/></div>Lost reports</div>
            <div class="card"><div class="num"><c:out value="${stats.found}"/></div>Found reports</div>
            <div class="card"><div class="num"><c:out value="${stats.matches}"/></div>Potential matches</div>
            <div class="card"><div class="num"><c:out value="${stats.approvedClaims}"/></div>Successful claims</div>
            <div class="card"><div class="num"><c:out value="${stats.pendingClaims}"/></div>Pending claims</div>
        </div>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/dashboard">Back</a></p>
</body>
</html>