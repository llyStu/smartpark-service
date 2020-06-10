<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = "//" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<base href="${basePath}">
</head>
<body>
	<%-- <div>${arg}</div> --%>
	<div>没有数据</div>
</body>
</html>
