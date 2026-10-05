<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Report - Lost &amp; Found</title>
</head>
<body>
    <h1>Edit report</h1>

    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <p>Type: <b><c:out value="${item.type}"/></b> (cannot be changed)</p>

    <form action="${pageContext.request.contextPath}/edit-item" method="post">
        <input type="hidden" name="id" value="${item.id}">

        <p>Title:<br>
            <input type="text" name="title" maxlength="150"
                   value="<c:out value='${item.title}'/>" required></p>

        <p>Category:<br>
            <select name="category" required>
                <option value="">-- choose --</option>
                <c:forEach var="cat" items="${['Wallet','Keys','Phone','Electronics','Documents','Bag','Clothing','Other']}">
                    <option value="${cat}" <c:if test="${item.category == cat}">selected</c:if>>${cat}</option>
                </c:forEach>
            </select></p>

        <p>Description:<br>
            <textarea name="description" rows="4" cols="40"><c:out value="${item.description}"/></textarea></p>

        <p>Color:<br>
            <input type="text" name="color" maxlength="50"
                   value="<c:out value='${item.color}'/>"></p>

        <p>Location:<br>
            <input type="text" name="location" maxlength="200"
                   value="<c:out value='${item.location}'/>" required></p>

        <p>Date:<br>
            <input type="date" name="itemDate"
                   value="<c:out value='${item.itemDate}'/>" required></p>

        <button type="submit">Save changes</button>
        <a href="${pageContext.request.contextPath}/my-reports">Cancel</a>
    </form>
</body>
</html>