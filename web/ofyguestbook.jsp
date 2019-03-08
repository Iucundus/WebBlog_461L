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
    <link type="text/css" rel="stylesheet" href="/stylesheets/bootstrap.min.css" />
</head>
<body  style="background-color: lightgray">
<%
    String guestbookName = request.getParameter("guestbookName");
    if (guestbookName == null) {
        guestbookName = "default";
    }
    String anonPost = request.getParameter("attemptedAnonPost");
    String all = request.getParameter("showAll");
    pageContext.setAttribute("showAll", false);
    if(all == null){
        pageContext.setAttribute("showAll", false);
    }
    else if (all.equals("true")) {
            pageContext.setAttribute("showAll", true);
    }
    pageContext.setAttribute("guestbookName", guestbookName);
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);
%>
<div style="background:#005cbf; margin-bottom: 0" class="jumbotron jumbotron-fluid d-flex justify-content-center">
    <img height="75px" width="75px" style="margin-right: 25px" src="goldenretriever.jpg">
<div class="h1 justify-content-center">Welcome to the DogBlog, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</div></div>
<%
} else {
%>
<div style="background:#005cbf; margin-bottom: 0" class="jumbotron jumbotron-fluid d-flex justify-content-center">
    <img height="75px" width="75px" style="margin-right: 25px" src="goldenretriever.jpg">
<div class="h1"><b>Hello!
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
    to include your name with greetings you post.</b></div></div>
<%
    }
%>
<%
    if(anonPost != null){
%>
<div style="background-color: lightcoral">Anonymous Posts not allowed. Login to post!</div>
<%
    }
%>
<%
    ObjectifyService.register(Greeting.class);
    List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class).list();
    Collections.sort(greetings);
    if (greetings.isEmpty()) {
%>
<div style="background:#5e6877; vertical-align: top" class="d-flex justify-content-center">
<div class="h2">DogBlog '${fn:escapeXml(guestbookName)}' has no messages.</div></div>
<%
} else {
%>
<div style="background:#5e6877; vertical-align: top" class="d-flex justify-content-center">
    <div class="h2"><b>Messages in DogBlog '${fn:escapeXml(guestbookName)}'.</b></div></div>
<%
    int i = 5;
    if(greetings.size() < 5 || pageContext.getAttribute("showAll").equals(true)){
       i = greetings.size();
    }
    for (int j = 0; j < i; j++) {
        Greeting greeting = greetings.get(greetings.size() - 1 - j);
        pageContext.setAttribute("greeting_title", greeting.getTitle());
        pageContext.setAttribute("greeting_content", greeting.getContent());
        pageContext.setAttribute("greeting_user", greeting.getUser());
%>
<div class="d-flex justify-content-center">
    <p class="h5" style="background:#c17f1b"><b>${fn:escapeXml(greeting_title)}</b> posted by: <b>${fn:escapeXml(greeting_user.nickname)}</b>:</p></div>
<div class="d-flex justify-content-center">
    <blockquote style="display: block; margin-right: 25px; margin-left: 25px"><%out.println(pageContext.getAttribute("greeting_content"));%></blockquote>
</div>
<%
        }
    }
%>
<% if (pageContext.getAttribute("showAll").equals(false)) {

%>
<div class = "d-flex justify-content-start">
    <a href="ofyguestbook.jsp?showAll=true">Show all Posts!</a>
</div>
<% } else { %>
<div class = "d-flex justify-content-start">
    <a href="ofyguestbook.jsp?showAll=false">Hide old posts!</a>
</div><%
}%>
<form action="/subscribers" method="post">
    <div><input type="submit" value="Subscribe to our Blog!" /></div>
    <input type="hidden" name="address" value="${fn:escapeXml(greeting_user.email)}"/>
</form>
<form action="/subscriberdelete" method="post">
    <div><input type="submit" value="Unsubscribe from our Blog!" /></div>
    <input type="hidden" name="address" value="${fn:escapeXml(greeting_user.email)}"/>
</form>
<form action="/ofysign.jsp" method="get">
    <div><input type="submit" value="Submit a new post!"/></div>
    <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>
</form>
</body>
</html>