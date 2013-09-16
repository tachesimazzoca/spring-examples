<%@ page isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("basedir", config.getServletContext().getContextPath()); %>
<c:import url="/WEB-INF/jsp/_layouts/default.jsp">
<c:param name="title" value="Home"/>
<c:param name="content">
<h2>Table of Contents</h2>
<h3>Basics</h3>
<ul>
  <li><a href="${basedir}/basics/plain">basics/plain</a></li>
  <li><a href="${basedir}/basics/headers">basics/headers</a></li>
</ul>
<h3>Pages</h3>
<ul>
  <li><a href="${basedir}/pages/help">pages/help</a></li>
  <li><a href="${basedir}/pages/about">pages/about</a></li>
</ul>
</c:param>
</c:import>
