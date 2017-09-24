<%@ page pageEncoding="utf-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>ping</title>
</head>
<body>
<h3>ping</h3>
<hr>

<table>
	<tr>
		<th>id</th>
		<th>url</th>
	</tr>
	<tr>
		<c:forEach items="${checks}" var="check">
			<td>${check.id}</td>
			<td>${check.url}</td>
		</c:forEach>
	</tr>
</table>

<hr>

<div><a href="${logoutUrl}">logout</a></div>

</body>
</html>
