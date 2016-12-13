<%@ page contentType="text/html;charset=UTF-8" language="java" import="ru.urfu.models.Message,java.util.Map" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Twitter</title>
        <link rel="stylesheet" type="text/css" href="/css/twitter.css"/>
        <script src="/js/jquery-3.1.1.min.js"></script>
        <script src="/js/mustache.min.js"></script>
        <script src="/js/templates.js"></script>
        <script src="/js/messages.js"></script>
        <script>
            window.data = {
                "messages": ${messagesJson}
            }
        </script>
    </head>
    <body onload = "onLoad()">
        <div class="content">
            <div class="title">My messages</div>
            <hr class="delim">
  		    <textarea placeholder="Enter new message" class="newMessageTextarea newMessageTextarea_folded" id="newMessageTextarea"></textarea>
  		    <div class="sendButtonWrapper">
  		        <button id="sendButton" class="sendButton" disabled="disabled">Send</button>
  		    </div>
            <div class="messages" id="messages"></div>
        </div>
    </body>
</html>