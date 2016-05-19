package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import database.*;

public class DBAccess
{

    public static boolean addUser(String userName, String passwd)
    {
        boolean exists;
        DBConnections dataSource = DBConnections.getInstance();
        Connection connection = dataSource.getConnection();
        ResultSet result = null;
        
        try
        {
            String statement = "SELECT password FROM User Where username= ? ";
            PreparedStatement pStat = connection.prepareStatement(statement);
            pStat.setString(1, userName);
            result = pStat.executeQuery();

            if (result.next())
            {
                exists = true;
            } else
            {
                exists = false;
            }
//            pStat.close();
//            connection.close();

        } catch (SQLException ex)
        {
            System.err.println("SQLException in Query.java");
            ex.printStackTrace(System.err);
            return false;
        }
        if (exists)
        {
            return false;
        } else
        {
            try
            {
                String statement = "INSERT INTO User (username, password, firstname) VALUES (?, MD5( ? ), '')";
                PreparedStatement pStat = connection.prepareStatement(statement);
                pStat.setString(1, userName);
                pStat.setString(2, passwd);
                pStat.executeUpdate();
                pStat.close();
                connection.close();

                return true;
            } catch (SQLException ex)
            {
                System.err.println("SQLException in Query.java");
                ex.printStackTrace(System.err);

                return false;
            }
        }
    }

    //missing a get function
    public static boolean addShipment(String userName, Shipment pack)
    {
        DBConnections dataSource = DBConnections.getInstance();
        Connection connection = dataSource.getConnection();

        try
        {

            String statement = "INSERT INTO Tracking_Number (Username, T_Number, "
                    + "Status, Carrier, Start_Location, Current_Location, Final_Location) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pStat = connection.prepareStatement(statement);
            pStat.setString(1, userName);
            pStat.setString(2, pack.getTrackingNumber());
            pStat.setString(3, pack.getStatus());
            pStat.setString(4, pack.getCarrier());
            pStat.setString(5, pack.getStartLocation());
            pStat.setString(6, pack.getCurrentLocation());
            pStat.setString(7, pack.getFinalLocation());

            pStat.executeUpdate();
            pStat.close();
            connection.close();

            return true;
        } catch (SQLException ex)
        {
            System.err.println("SQLException in Query.java");
            ex.printStackTrace(System.err);

            return false;
        }
    }

    public static boolean removeShipment(String userName, String trackingNumber)
    {
        DBConnections dataSource = DBConnections.getInstance();
        Connection connection = dataSource.getConnection();

        try
        {
            String statement = "DELETE FROM Tracking_Number WHERE Username = '" + userName + "' "
                    + "AND  T_Number= '" + trackingNumber + "'";
            Statement stat = connection.createStatement();
            int num = stat.executeUpdate(statement);
            connection.close();

            if (num == 1)
            {
                return true;
            } else
            {
                return false;
            }
        } catch (SQLException ex)
        {
            System.err.println("SQLException in Query.java");
            ex.printStackTrace(System.err);

            return false;
        }

    }

    //needs to parse and update shipment before adding it to history
    public static History getHistory(String userName)
    {
        History history = new History();
        DBConnections dataSource = DBConnections.getInstance();
        Connection connection = dataSource.getConnection();

        try
        {
            String statement = "SELECT T_Number FROM Tracking_Number WHERE Username = '" + userName + "'";
            Statement stat = connection.createStatement();
            ResultSet result = stat.executeQuery(statement);

            while (result.next())
            {
                Statement stat2 = connection.createStatement();
                String trackingNumber = result.getString("T_Number");
                Shipment shipment = new Shipment(trackingNumber);
                history.addToHistory(userName, shipment);

                String status = shipment.getStatus();
                String location = shipment.getCurrentLocation();
                String carrier = shipment.getCarrier();
                String start = shipment.getStartLocation();
                String finalL = shipment.getFinalLocation();
                String date = shipment.getDateInput();
                String update = "UPDATE Tracking_Number SET Status = '" + status + "', "
                        + "Current_Location = '" + location + "', "
                        + "Carrier = '" +carrier+ "',"
                        + "Date_Input = '" +date+ "',"
                        + "Start_Location = '" +start+ "',"
                        + "Final_Location = '" +finalL+ "' "
                        + "WHERE Username = '" + userName + "' AND "
                        + "T_Number = '" + shipment.getTrackingNumber() + "'";
                stat2.executeUpdate(update);
            }

            connection.close();

        } catch (SQLException ex)
        {
            System.err.println("SQLException in Query.java");
            ex.printStackTrace(System.err);
        }

        return history;
    }

    public static boolean isValidUser(String userName, String passwd)
    {
        DBConnections dataSource = DBConnections.getInstance();
        Connection connection = dataSource.getConnection();
        //Statement stat = null;
        ResultSet result = null;

        try
        {
            //stat = connection.createStatement();
            String statement = "SELECT * FROM User Where username= ? AND password=MD5( ? )";
            PreparedStatement pStat = connection.prepareStatement(statement);
            pStat.setString(1, userName);
            pStat.setString(2, passwd);
            result = pStat.executeQuery();
            //result = stat.executeQuery("SELECT * FROM User Where Username='"+userName+"' AND Password='"+passwd+"'");
            if (result.next())
            {
                return true;
            } else
            {
                return false;
            }
        } catch (SQLException ex)
        {
            System.err.println("SQLException in Query.java");
            ex.printStackTrace(System.err);

            return false;
        }
    }
}
