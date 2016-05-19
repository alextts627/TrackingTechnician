<%-- 
    Document   : signUp
    Created on : Nov 6, 2011, 5:30:18 PM
    Author     : Read
Jsp to ask for user password twice and the user name, will call createUser servelt
pass "pass1", "pass2", and "userName" in the form (get or post) 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="database.*,model.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tracking Technician: Sign Up</title>
    </head>
    <body>       
        <p> 
            <input type="button" value="Main Page" name="home" onclick="window.location.href='index.jsp'"/>
        </p>
        </br>
        <h2 align="center"> Sign Up!</h2>
        </br></br></br></br></br></br>
        <form method=POST action="createUser">
            <p align="center">
            <table align="center">
                <tr>
                    <td align="right">User Email: </td>
                    <td><input type="text" name="email" align="right"/></td>
                </tr>
                <tr>
                    <td align="right">Password: </td>
                    <td><input type="password" name="pass1" align="right"/></td>
                </tr>
                <tr>
                    <td align="right">Confirm Password: </td>
                    <td><input type="password" name="pass2" align="right"/></td>
                </tr>
            </table>
            </p>
            <p align="center">
                <input type="submit" value="Submit" align="center"/>
            </p>
        </form>
    </body>
</html>
