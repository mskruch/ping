<%@ page pageEncoding="utf-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>ping - admin</title>
</head>
<body>
<h3>admin</h3>
<hr>

<table>
    <tr>
        <th>id</th>
        <th>email</th>
        <th>enabled</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.email}</td>
            <td><a href="<c:url value="/admin/${user.id}" />">${user.enabled}</a></td>
        </tr>
    </c:forEach>
</table>
<h4>config</h4>
<table>
    <tr>
        <th>id</th>
        <th>key</th>
        <th>value</th>
    </tr>
    <c:forEach items="${configEntries}" var="entry">
        <tr>
            <td>${entry.id}</td>
            <td><c:out value="${entry.key}"/></td>
            <td><c:out value="${entry.value}"/></td>
        </tr>
    </c:forEach>
</table>

<hr>

<div><a href="<c:url value="/" />">main</a></div>

</body>
</html>
