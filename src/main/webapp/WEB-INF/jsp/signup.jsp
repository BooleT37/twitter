<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head></head>
<body>
   <h1>Signup</h1>
      <c:if test="${errorMessage != null}">
          <div>
              Error creating a user<br/>
              Reason: <c:out value="${errorMessage}" />
          </div>
      </c:if>
   <form name='signup' action="signup" method='POST'>
      <table>
         <tr>
            <td>Login:</td>
            <td><input type='text' name='username' value=''></td>
         </tr>
         <tr>
            <td>Password:</td>
            <td><input type='password' name='password' /></td>
         </tr>
         <tr>
            <td>Repeat password:</td>
            <td><input type='password' name='repeatPassword' /></td>
         </tr>
         <tr>
            <td><input name="submit" type="submit" value="submit" /></td>
         </tr>
      </table>
  </form>
</body>
</html>
