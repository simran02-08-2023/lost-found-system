<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${item.title}"/> - Lost &amp; Found</title>
</head>
<body>
    <h1><c:out value="${item.title}"/></h1>

    <table cellpadding="6">
        <tr><td><b>Type</b></td><td><c:out value="${item.type}"/></td></tr>
        <tr><td><b>Category</b></td><td><c:out value="${item.category}"/></td></tr>
        <tr><td><b>Color</b></td><td><c:out value="${item.color}"/></td></tr>
        <tr><td><b>Location</b></td><td><c:out value="${item.location}"/></td></tr>
        <tr><td><b>Date</b></td><td><c:out value="${item.itemDate}"/></td></tr>
        <tr><td><b>Status</b></td><td><c:out value="${item.status}"/></td></tr>
        <tr><td><b>Description</b></td><td><c:out value="${item.description}"/></td></tr>
    </table>

    <c:if test="${isOwner}">
        <p><i>This is your report.</i></p>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/items">Back to items</a></p>
</body>
</html>