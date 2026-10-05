<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Lost &amp; Found</title>
</head>
<body>
    <h1>Login</h1>

    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <p>Email:<br>
            <input type="email" name="email" value="<c:out value='${email}'/>" required></p>
        <p>Password:<br>
            <input type="password" name="password" required></p>
        <button type="submit">Login</button>
    </form>

    <p>No account? <a href="${pageContext.request.contextPath}/register">Register</a></p>
</body>
</html>