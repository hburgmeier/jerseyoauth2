<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Authorize Client Application</title>
</head>
<body>
	<h1>Authorize Client Application</h1>
	<p>Do you trust the application <c:out value="${clientApp.applicationName}"/> ?</p>
	<p>It requested the following permissions:<p>
	<ul>
	<c:forEach var="scopeItem" items="${scope}">
		<li>${scopeItem}</li>
	</c:forEach>	
	</ul>
	<form action="<c:url value="/oauth2/allow" />" method="post">
		<input type="hidden" name="client_id" value="${clientApp.clientId}" />
		<input type="hidden" name="scope" value="${scopes}" />
		<button type="submit">Yes</button>
	</form>
</body>
</html>