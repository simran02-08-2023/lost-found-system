<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Report Item - Lost &amp; Found</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jspf" %>
    <h1>Report an item</h1>

    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/report-item" method="post">

        <p>
            <label><input type="radio" name="type" value="LOST"
                <c:if test="${type == 'LOST'}">checked</c:if> required> Lost</label>
            <label><input type="radio" name="type" value="FOUND"
                <c:if test="${type == 'FOUND'}">checked</c:if>> Found</label>
        </p>

        <p>Title:<br>
            <input type="text" name="title" maxlength="150"
                   value="<c:out value='${title}'/>" required></p>

        <p>Category:<br>
            <select name="category" required>
                <option value="">-- choose --</option>
                <c:forEach var="c" items="${['Wallet','Keys','Phone','Electronics','Documents','Bag','Clothing','Other']}">
                    <option value="${c}" <c:if test="${category == c}">selected</c:if>>${c}</option>
                </c:forEach>
            </select></p>

        <p>Description:<br>
            <textarea name="description" rows="4" cols="40"><c:out value="${description}"/></textarea></p>

        <p>Color:<br>
            <input type="text" name="color" maxlength="50"
                   value="<c:out value='${color}'/>"></p>

        <p>Location:<br>
            <input type="text" name="location" maxlength="200"
                   value="<c:out value='${location}'/>" required></p>

        <p>Date:<br>
            <input type="date" name="itemDate"
                   value="<c:out value='${itemDate}'/>" required></p>

        <button type="submit">Submit report</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/dashboard">Back to dashboard</a></p>
</body>
</html>