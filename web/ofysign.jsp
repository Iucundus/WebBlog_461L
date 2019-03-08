<%--
  Created by IntelliJ IDEA.
  User: zachary
  Date: 3/7/19
  Time: 5:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.googlecode.objectify.*" %>
<%@ page import="java.util.Collections" %>
<%@ page import="guestbook.Greeting" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
    <link type="text/css" rel="stylesheet" href="/stylesheets/bootstrap.min.css" />
</head>
<body>
<div style="background:#005cbf; margin-bottom: 0" class="jumbotron jumbotron-fluid d-flex justify-content-center">
    <img height="75px" width="75px" style="margin-right: 25px" src="goldenretriever.jpg">
    <div class="h1 justify-content-center">Write your post!</div></div>
<%
    pageContext.setAttribute("guestbookName",request.getParameter("guestbookName"));
%>
<div style="background:#5e6877; vertical-align: top" class="d-flex justify-content-center">
<form action="/ofysign" method="post">

    <div style="background:#c17f1b">Post Title: <div class="h5"><textarea name="title" rows="1" cols="60"></textarea></div></div>
    <div style="background:#c17f1b">Post Content: <div class="h5"><textarea name="content" rows="3" cols="60"></textarea></div></div>
    <div><input type="submit" value="Post Greeting" /></div>
    <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>
</form>
</div>
</body>
</html>
