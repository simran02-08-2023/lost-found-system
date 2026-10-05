<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Lost &amp; Found</title>
</head>
<body>
    <h1>Hello, <c:out value="${sessionScope.userName}"/>!</h1>
    <p>You are logged in. Your role: <c:out value="${sessionScope.role}"/></p>

    <p>
        <a href="${pageContext.request.contextPath}/report-item">Report lost/found item</a> |
        <a href="${pageContext.request.contextPath}/items">Browse items</a> |
        <a href="${pageContext.request.contextPath}/my-reports">My reports</a> |
        <a href="${pageContext.request.contextPath}/matches">Matches</a>
    </p>

    <c:if test="${sessionScope.role == 'ADMIN'}">
        <p><a href="${pageContext.request.contextPath}/admin/dashboard">Admin panel</a></p>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/logout">Logout</a></p>
</body>
</html>