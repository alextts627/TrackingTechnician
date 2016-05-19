<%-- 
    Document   : mapAndInfo
    Created on : Nov 6, 2011, 5:27:58 PM
    Author     : Read
Called by the processingTracking servlet and the user's history page
Should display a google map and associate data with the package

NOTE: The shipment to be displayed is stored in the session as
"processShipment".  Please remove it from the session at the bottom of this jsp.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="database.*,model.*"%>
<%
    Shipment shipment = (Shipment) session.getAttribute("processShipment");
    User user = (User) session.getAttribute("user");
    session.removeAttribute("processShipment"); //removed so future processShipment's won't conflict
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Map and Info</title>
    </head>
    <body>
        <% 
            //User user = (User) session.getAttribute("user");
            //if(!user.isLoggedIn())
            if(user.isLoggedIn())
            {
        %>
        <p> 
            <input type="button" value="Main Page" name="home" onclick="window.location.href='index.jsp'"/>                     
            <input type="button" value="View History" name="" onclick="window.location.href='history.jsp'"/>
        </p>
        <%
            }
            else 
            {

        %>
        <p> 
            <input type="button" value="Main Page" name="home" onclick="window.location.href='index.jsp'"/>                     
            
        </p>
        <%
            }
        %>
         
        </br>
        <p align ="center">
            <font size ="5" color="blue">
            Details of package delivery so far:
            </font>
        </p>
        </br>
        <table align ="center" border="1">
            <tr>
                <td>carrier :</td>
                
                <td>"<%=shipment.getCarrier() %>"</td>
            </tr>
            <tr>
                <td>Starting Location</td>
                <td>"<%=shipment.getStartLocation()%>"</td>
            </tr>
            <tr>
                <td>Destination</td>
                <td>"<%=shipment.getFinalLocation() %>"</td>
            </tr>
            <tr>
                <td>Current Location</td>
                <td>"<%=shipment.getCurrentLocation()%>" </td>
            </tr>
            <tr>
                <td>Tracking #</td>
                <td>"<%=shipment.getTrackingNumber() %>"</td>
            </tr>
            <tr>
                <td>Status</td>
                <td>"<%=shipment.getStatus() %>"</td>
            </tr>
        </table>
            <p align="center">
        <img border="2" src="<%= shipment.getGoogleMapURL() %>"</img>
            </p>
      </p>
    </body>
</html>
