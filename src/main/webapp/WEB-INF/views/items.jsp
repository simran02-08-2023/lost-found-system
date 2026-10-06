<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Items - Lost &amp; Found</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jspf" %>
    <h1>Browse items</h1>

    <c:if test="${not empty param.reported}">
        <p style="color:green">Your report was saved.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/items" method="get">
        <input type="text" name="q" placeholder="Search words"
               value="<c:out value='${param.q}'/>">

        <select name="type">
            <option value="">All types</option>
            <option value="LOST" <c:if test="${param.type == 'LOST'}">selected</c:if>>Lost</option>
            <option value="FOUND" <c:if test="${param.type == 'FOUND'}">selected</c:if>>Found</option>
        </select>

        <select name="category">
            <option value="">All categories</option>
            <c:forEach var="cat" items="${['Wallet','Keys','Phone','Electronics','Documents','Bag','Clothing','Other']}">
                <option value="${cat}" <c:if test="${param.category == cat}">selected</c:if>>${cat}</option>
            </c:forEach>
        </select>

        <input type="text" name="location" placeholder="Location"
               value="<c:out value='${param.location}'/>">

        <button type="submit">Search</button>
        <a href="${pageContext.request.contextPath}/items">Clear</a>
    </form>

    <br>

    <c:if test="${empty items}">
        <p>No items found.</p>
    </c:if>

    <c:if test="${not empty items}">
        <table border="1" cellpadding="6">
            <tr>
                <th>Type</th><th>Title</th><th>Category</th>
                <th>Color</th><th>Location</th><th>Date</th><th></th>
            </tr>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.type}"/></td>
                    <td><c:out value="${item.title}"/></td>
                    <td><c:out value="${item.category}"/></td>
                    <td><c:out value="${item.color}"/></td>
                    <td><c:out value="${item.location}"/></td>
                    <td><c:out value="${item.itemDate}"/></td>
                    <td><a href="${pageContext.request.contextPath}/item-details?id=${item.id}">View</a></td>
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