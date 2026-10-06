<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Reports - Lost &amp; Found</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jspf" %>
    <h1>My reports</h1>

    <c:if test="${not empty param.closed}">
        <p style="color:green">Report closed.</p>
    </c:if>
    <c:if test="${not empty param.failed}">
        <p style="color:red">That report could not be closed.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <c:if test="${empty items}">
        <p>You have not reported anything yet.</p>
    </c:if>

    <c:if test="${not empty items}">
        <table border="1" cellpadding="6">
            <tr>
                <th>Type</th><th>Title</th><th>Location</th>
                <th>Date</th><th>Status</th><th>Actions</th>
            </tr>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.type}"/></td>
                    <td><c:out value="${item.title}"/></td>
                    <td><c:out value="${item.location}"/></td>
                    <td><c:out value="${item.itemDate}"/></td>
                    <td><c:out value="${item.status}"/></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/item-details?id=${item.id}">View</a>
                        <c:if test="${item.status == 'ACTIVE'}">
                            <form action="${pageContext.request.contextPath}/close-item"
                                  method="post" style="display:inline"
                                  onsubmit="return confirm('Close this report?');">
                                <input type="hidden" name="id" value="${item.id}">
                                <button type="submit">Close</button>
                            </form>
                        </c:if>
                    </td>
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