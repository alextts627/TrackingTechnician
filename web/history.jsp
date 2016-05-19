<%-- 
    Document   : history
    Created on : Nov 6, 2011, 5:28:31 PM
    Author     : Read

show the logged in user's history page
--%>
<%@ page import="database.*,model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    //DBConnections dataSource = DBConnections.getInstance();
    //Connection connection = dataSource.getConnection();
    //Statement statement = null;
    //ResultSet rs = null;
    out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
    out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
    out.println("<html  xmlns = \"http://www.w3.org/1999/xhtml\">");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>History Page</title>
</head>
<body>
    <% User user = (User) session.getAttribute("user");%>
    <%-- uncomment the next line for testing --%>
    <% //User user = new User("BoB"); %>
    <p> 
        <input type="button" value="Main Page" name="home" onclick="window.location.href='index.jsp'"/> 
    </p>
    <br/>

    <h3 align="center">Return to main page to add another tracking number.<br/>
        Click a number to go to its Map and Info or choose to delete a number</h3>

    <br/><br/>
    <h2 align="center"><%= user.getUserName()%> </h2>
    <%
        //statement = connection.createStatement();
        //rs = statement.executeQuery("SELECT T_Number, Date_Input "
        //+ "FROM Tracking_Number WHERE Username = '"+user.getUserName()+"'");
        History history = user.getHistory();
        if (!history.isEmpty())
        {
    %>
    <table align="center" border="1">
        <tr>
            <th>Tracking Numbers</th>
            <th>Shipping Co. & Date</th>
            <th>Delete #?</th>
        </tr>           
        <%
            String trackingNumber;
            String dateInput;
            String carrier;
            java.util.Iterator iter = history.values().iterator();
            while (iter.hasNext())
            {
                Shipment shipment = (Shipment) iter.next();
                trackingNumber = shipment.getTrackingNumber();
                dateInput = shipment.getDateInput();
                carrier = shipment.getCarrier();

                //session.setAttribute("processShipment", shipment);
        %>

        <tr>
            <td>
                <form method="post" action="processTracking">
                    <input style="width: 200px" align="center" type="submit" name="trackingNumber" value ="<%= trackingNumber%>"/>
                </form>
            </td>
            <td align="center"> <%= carrier%>   <%= dateInput%> </td>
            <td align="center">
                <form action="removeShipment"method="post" > 
                    <input align="center" type="submit" value="Delete"/>
                    <input type="hidden" name="selected" value="<%= trackingNumber%>"/>
                </form>

            </td>  
        </tr>
        <% }%>
    </table>
    <% } else
    {
    %>
    <p align="center"> Please go back to the main page and enter a tracking number </p>
    <% }%>
</form>

</body>
</html>
