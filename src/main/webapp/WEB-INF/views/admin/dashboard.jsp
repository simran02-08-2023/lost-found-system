<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Lost &amp; Found</title>
</head>
<body>
    <h1>Admin dashboard</h1>
    <p>Welcome, <c:out value="${sessionScope.userName}"/>. Only admins can see this page.</p>
    <p><a href="${pageContext.request.contextPath}/dashboard">Back</a></p>
</body>
</html>