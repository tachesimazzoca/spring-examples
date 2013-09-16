<%@ page
  pageEncoding="UTF-8"
  contentType="text/html; charset=utf-8"
  isELIgnored="false"
  trimDirectiveWhitespaces="true"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("basedir", config.getServletContext().getContextPath()); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><c:out value="${param.title}"/> | Spring Framework Web</title>
</head>
<body>
<div><a href="${basedir}/">Spring Framework Web</a></div>
<hr>
${param.content}
</body>
</html>
