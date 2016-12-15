<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>
    <head>
        <title>Twitter - ошибка 404</title>
    </head>
    <body>
        <h2>Упс! Что-то пошло не так.</h2><br/>
        Ошибка ${error}!<br/>
        Код ошибки ${status}<br/>
        Exception: ${exception}<br/>
        Error message: ${message}<br/>
        <br/><br/>Вы можете <a href ="/">вернуться на главную страницу</a>
    </body>
</html>