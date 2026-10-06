<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Matches - Lost &amp; Found</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jspf" %>
    <h1>Potential matches</h1>

    <c:if test="${not empty param.newMatches}">
        <p style="color:green">Your report was saved and we found
            <c:out value="${param.newMatches}"/> possible match(es)!</p>
    </c:if>
    <c:if test="${not empty param.checked}">
        <p style="color:green">Matches updated.</p>
    </c:if>
    <c:if test="${not empty param.rejected}">
        <p style="color:green">Match removed.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/matches" method="post">
        <input type="hidden" name="action" value="recheck">
        <button type="submit">Re-check my items for matches</button>
    </form>
    <br>

    <c:if test="${empty matches}">
        <p>No matches yet. Report an item and we will look for matches.</p>
    </c:if>

    <c:forEach var="m" items="${matches}">
        <div style="border:1px solid #888; padding:12px; margin-bottom:16px; max-width:640px">
            <h2>
                <fmt:formatNumber value="${m.score}" maxFractionDigits="0"/>% -
                <c:out value="${m.label}"/>
            </h2>

            <table cellpadding="6">
                <tr>
                    <td><b>Lost item</b></td>
                    <td><c:out value="${m.lost.title}"/> -
                        <c:out value="${m.lost.location}"/>,
                        <c:out value="${m.lost.itemDate}"/></td>
                </tr>
                <tr>
                    <td><b>Found item</b></td>
                    <td><c:out value="${m.found.title}"/> -
                        <c:out value="${m.found.location}"/>,
                        <c:out value="${m.found.itemDate}"/></td>
                </tr>
            </table>

            <p><b>Reasons:</b></p>
            <ul style="list-style:none; padding-left:8px">
                <c:forEach var="reason" items="${m.reasons}">
                    <li>&#10003; <c:out value="${reason}"/></li>
                </c:forEach>
            </ul>

            <a href="${pageContext.request.contextPath}/item-details?id=${m.lost.id}">View lost item</a> |
            <a href="${pageContext.request.contextPath}/item-details?id=${m.found.id}">View found item</a>

            <form action="${pageContext.request.contextPath}/matches" method="post"
                  style="display:inline"
                  onsubmit="return confirm('Remove this match?');">
                <input type="hidden" name="action" value="reject">
                <input type="hidden" name="id" value="${m.matchId}">
                <button type="submit">Not a match</button>
            </form>
        </div>
    </c:forEach>

    <p><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></p>
</body>
</html>