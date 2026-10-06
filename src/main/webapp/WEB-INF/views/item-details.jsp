<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${item.title}"/> - Lost &amp; Found</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/navbar.jspf" %>
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

    <%-- FINDER VIEW: claims on my own FOUND item --%>
    <c:if test="${isOwner && item.type == 'FOUND'}">
        <hr>
        <h2>Claims on this item</h2>

        <c:if test="${empty itemClaims}">
            <p>No one has claimed this item yet.</p>
        </c:if>

        <c:forEach var="cl" items="${itemClaims}">
            <div style="border:1px solid #888; padding:10px; margin-bottom:12px; max-width:560px">
                <p><b>Status:</b> <c:out value="${cl.status}"/></p>
                <p><b>Sent:</b> <c:out value="${cl.createdAt}"/></p>
                <p><b>What the claimant says:</b><br><c:out value="${cl.description}"/></p>
            </div>
        </c:forEach>

        <c:if test="${not empty itemClaims}">
            <p><i>An admin reviews each claim before the item is handed over.</i></p>
        </c:if>
    </c:if>

    <%-- CLAIMANT VIEW: claim form on someone else's ACTIVE FOUND item --%>
    <c:if test="${item.type == 'FOUND' && item.status == 'ACTIVE' && !isOwner}">
        <hr>
        <h2>Is this yours?</h2>

        <c:choose>
            <c:when test="${param.claimError == 'short'}">
                <p style="color:red">Please write at least 10 characters.</p>
            </c:when>
            <c:when test="${param.claimError == 'exists'}">
                <p style="color:red">You already have a pending claim on this item.</p>
            </c:when>
            <c:when test="${param.claimError == 'own'}">
                <p style="color:red">You cannot claim your own report.</p>
            </c:when>
            <c:when test="${param.claimError == 'unavailable'}">
                <p style="color:red">This item can no longer be claimed.</p>
            </c:when>
        </c:choose>

        <form action="${pageContext.request.contextPath}/claims" method="post">
            <input type="hidden" name="itemId" value="${item.id}">
            <p>Why do you believe this belongs to you?<br>
               Add details only the owner would know (marks, contents, and so on).</p>
            <textarea name="description" rows="4" cols="50" required minlength="10" maxlength="1000"></textarea>
            <br><br>
            <button type="submit">Claim this item</button>
        </form>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/items">Back to items</a></p>
</body>
</html>