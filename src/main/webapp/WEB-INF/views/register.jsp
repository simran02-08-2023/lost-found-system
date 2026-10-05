<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register - Lost &amp; Found</title>
</head>
<body>
    <h1>Create account</h1>

    <c:if test="${not empty param.success}">
        <p style="color:green">Registered successfully!</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <p>Name:<br>
            <input type="text" name="name" value="<c:out value='${name}'/>" required></p>
        <p>Email:<br>
            <input type="email" name="email" value="<c:out value='${email}'/>" required></p>
        <p>Password (min 8 characters):<br>
            <input type="password" name="password" required minlength="8"></p>
        <button type="submit">Register</button>
    </form>
</body>
</html>