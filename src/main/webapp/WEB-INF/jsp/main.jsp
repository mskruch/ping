<%@ page pageEncoding="utf-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>ping</title>
</head>
<body>
<h3>ping</h3>
<hr>

<c:if test="${checks != null}">
    <table>
        <tr>
            <th>id</th>
            <th>url</th>
            <th>checked</th>
            <th>status</th>
        </tr>
        <c:forEach items="${checks}" var="check">
            <tr>
                <td>${check.id}</td>
                <td><c:out value="${check.url}"/></td>
                <td>${check.lastCheckDuration}</td>
                <td>${check.status} since ${check.statusSinceDuration}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${checks == null}">
    <p>Please ask the administrator to enable the account.</p>
</c:if>


<hr>
<c:if test="${admin}">
    <div><a href="<c:url value="/admin" />">admin</a></div>
</c:if>

<div><a href="${logoutUrl}">logout</a></div>

</body>
</html>
