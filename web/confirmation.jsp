<%-- 
    Document   : confirmation
    Created on : Nov 6, 2011, 7:28:45 PM
    Author     : AlexTsao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="database.*,model.*"%>

<%
    User user = (User) session.getAttribute("user");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmation?</title>
    </head>
    <body>
        <% 
            //User user = (User) session.getAttribute("user");
            //if(!user.isLoggedIn())
            if(!user.isLoggedIn())
            {
        %>
        
        
        <h2 align="center"> Create Account Failure </h2>
        
        
        
        <p align ="center">            
            </br></br></br></br></br></br>
            You have failed to create an account..</br>
            You may try again or return to the main page.</br></br></br></br></br>
                
       </p>

       <p align ="center"> 
           <input type="button" value="Main Page" name="home" onclick="window.location.href='index.jsp'"/> 
           <input type="button" value="Create Account" name="home" onclick="window.location.href='signUp.jsp'">
        
       </p>
       <%
            }
            else 
            {

       %>
         
         <h1><p align = "center"> Successfully Logged In! </p></h1>
        
         <% user.getUserName(); //this is for testing %>
             
            <p border="1" align ="center">            
                </br></br></br></br></br></br>
                You have successfully Registered to</br>
                Tracking Technicians.</br></br></br></br></br>
                
            </p>
       
            <p align ="center"> 
                <input type="button" value="Main Page" name="home" onclick="window.location.href='index.jsp'"/> 
                        
            </p>
       <%
             }
       %>
    </body>
</html>
