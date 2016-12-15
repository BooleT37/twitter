<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head></head>
<body>
   <h1>Login</h1>
      <c:if test="${param.error != null}">
          <div>
              Authentication error
              <c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}"><br/>
                Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
              </c:if>
          </div>
      </c:if>
      <c:if test="${param.logout != null}">
          <div>
              You have successfully logged out.
          </div>
      </c:if>
      <c:if test="${param.newUser != null}">
           <div>
               New user ${newUser} has beed successfully created. Use login and password to enter
           </div>
      </c:if>
   <form name='f' action="login" method='POST'>
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
            <td><input name="submit" type="submit" value="submit" /></td>
         </tr>
      </table>
  </form>
  <br/>
  Don't have an account? <a href="/signup">Sign up</a>
</body>
</html>
