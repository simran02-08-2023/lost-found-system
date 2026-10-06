<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Claims - Lost &amp; Found</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jspf" %>
    <h1>My claims</h1>

    <c:if test="${not empty param.submitted}">
        <p style="color:green">Your claim was sent. An admin will review it.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <c:if test="${empty claims}">
        <p>You have not made any claims yet.</p>
    </c:if>

    <c:if test="${not empty claims}">
        <table border="1" cellpadding="6">
            <tr><th>Item</th><th>Your explanation</th><th>Status</th><th>Sent</th></tr>
            <c:forEach var="cl" items="${claims}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/item-details?id=${cl.itemId}">
                        <c:out value="${cl.itemTitle}"/></a></td>
                    <td><c:out value="${cl.description}"/></td>
                    <td><c:out value="${cl.status}"/></td>
                    <td><c:out value="${cl.createdAt}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <p>
        <a href="${pageContext.request.contextPath}/items">Browse items</a> |
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
    </p>
</body>
</html>