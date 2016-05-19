<%-- 
    Document   : index
    Created on : Oct 14, 2011, 12:55:08 PM
    Author     : Austin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="database.*,model.*"%>
<%-- //uncomment this in working code, see below for test code
       // User user = new User();
        //session.setAttribute("user", user);
        
--%>


<html>   
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tracking Technician</title>
    </head>
    <body>
        <%
            User user = (User) session.getAttribute("user");
            if (user == null)
            {//then this is the first time the User has accessed the site/they are not logged in.
                user = new User();
                session.setAttribute("user", user);
            }
            if (!user.isLoggedIn())
            {

        %>

        <p align="left"> Guest </p>     

        <form method="post" action="LogIn"> 
        <%-- to test login, Email= ts1, password = becky --%>
            <table align="right">
                <tr>
                    <td>Email: </td>
                    <td><input type="text" name="userName" size="20"/></td>
                </tr>
                <tr>
                    <td>Password: </td>
                    <td><input type="password" name="password" size="20"/></td>
                </tr>
            </table>
            <br/><br/><br/>
            <p align="right">
                <input style="width: 65px" type="submit" value="Log In"/>
                <input style="width: 65px" type="button" value="Sign Up" onclick="window.location.href='signUp.jsp'"/> 
            </p>
        </form>   

<!--        <p align="right">
            <input type="button" value="Sign Up" onclick="window.location.href='signUp.jsp'"/>  
        </p>-->
        <% 
            } else
            {

        %>
        <p align="left"> <%= user.getUserName()%> </p>       
        <div align="right">
            
            <input style="width: 90px" type="button" value="Log Out" onclick="window.location.href='LogOut'"/>
            <input style="width: 90px" type="button" value="View History" onclick="window.location.href='history.jsp'"/>
        </div>
        </p>
        <% }%>
        <p align="center">
            <img src="logo.gif" height="110" width="823"  alt="Logo"/>
        </p>
        <p align="center">
            Welcome to Tracking Technician! Enter a tracking number below to get<br/>
            detailed information about your shipment, or login/create an account<br/>
            to keep track of many shipments at once.<br/>
        </p>
        <br/>
        <form action="processTracking">
            <p align="center">
                Enter Tracking Number: <input type="text" name="trackingNumber" size="30"/>

                <input type="submit" value="   Submit   "/>
            </p>
        </form>
    </body>
</html>
