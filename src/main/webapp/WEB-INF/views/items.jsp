<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Items - Lost &amp; Found</title>
</head>
<body>
    <h1>All active items</h1>

    <c:if test="${not empty param.reported}">
        <p style="color:green">Your report was saved.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <c:if test="${empty items}">
        <p>No items yet.</p>
    </c:if>

    <c:if test="${not empty items}">
        <table border="1" cellpadding="6">
            <tr>
                <th>Type</th><th>Title</th><th>Category</th>
                <th>Color</th><th>Location</th><th>Date</th>
            </tr>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.type}"/></td>
                    <td><c:out value="${item.title}"/></td>
                    <td><c:out value="${item.category}"/></td>
                    <td><c:out value="${item.color}"/></td>
                    <td><c:out value="${item.location}"/></td>
                    <td><c:out value="${item.itemDate}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <p>
        <a href="${pageContext.request.contextPath}/report-item">Report an item</a> |
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
    </p>
</body>
</html>