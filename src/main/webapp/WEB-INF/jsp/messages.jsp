<%@ page contentType="text/html;charset=UTF-8" language="java" import="ru.urfu.models.Message,java.util.Map" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>
    <link rel="stylesheet" type="text/css" href="/twitter.css"/>
    <body>
        <h1>twitter</h1>
        This is your twitter application
        <ul class="messages">
            <c:forEach var="message" items="${messages}">
                <c:out value="${message.getValue().getContent()}"/> <br/>
            </c:forEach>
        </ul>
    </body>
</html>