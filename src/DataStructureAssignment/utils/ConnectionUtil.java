package DataStructureAssignment.utils;

import DataStructureAssignment.controller.LoginController;

import java.sql.*;

public class ConnectionUtil {
    //sets the username and password of the remote database
    private final static String  username1 = "sql12348167";
    private final static String password1 = "lsrX65sERp";

    private final static String username2 = "sql12348306";
    private final static String password2 = "tWbiKxPxJu";

    public static Connection conDB1() {
        try {
            //register the driver
            Class.forName("com.mysql.jdbc.Driver");

            //get the connection from the remote database
            Connection con = DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net/sql12348167" , username1, password1);
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("ConnectionUtil : " + ex.getMessage());
            return null;
        }
    }

    public static Connection conDB2() {
        try {
            //register the driver
            Class.forName("com.mysql.jdbc.Driver");

            //get the connection from the remote database
            Connection con = DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net/sql12348306" , username2, password2);
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("ConnectionUtil : " + ex.getMessage());
            return null;
        }
    }

    public static String getAreaDetail( int recordID, int day) throws SQLException {
        //get the area detail from the areaDetail table of second database
        Connection conn = conDB2();
        String sql = "SELECT areaDetails FROM area WHERE BINARY username = ? AND id = ? AND day = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        preparedStatement.setInt(3, day);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }else{
            return "";
        }
    }

    public static String getKangarooDetail( int recordID, int day) throws SQLException {
        //get the kangaroo detail from the kangarooDetail table of second database
        Connection conn = conDB2();
        String sql = "SELECT kangarooDetails FROM kangaroo WHERE BINARY username = ? AND id = ? AND day = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        preparedStatement.setInt(3, day);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }else{
            return "";
        }

    }
    public static String getSummary(int recordID,int day) throws SQLException {
        //get the summary from the summaries table of first database
        Connection conn = conDB1();
        String sql = "SELECT summary FROM summaries WHERE BINARY username = ? AND id = ? AND day = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        preparedStatement.setInt(3,day);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }else{
            return "";
        }
    }
    public static String getJumpingHistory( int recordID) throws SQLException {
        //get the jumping history of the kangaroos from the summary table of second database
        Connection conn = conDB2();
        String sql = "SELECT jumpingLogs FROM logs WHERE BINARY username = ? AND id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }else{
            return "";
        }
    }
    public static String getPathDetails( int recordID) throws SQLException {
        //get the jumping history of the kangaroos from the summary table of second database
        Connection conn = conDB2();
        String sql = "SELECT pathDetails FROM logs WHERE BINARY username = ? AND id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }else{
            return "";
        }
    }

    public static void insertAreaDetail( int recordID, int day,String text) throws SQLException {
        //get the area detail from the areaDetail table of second database
        Connection conn = conDB2();
        String sql = "INSERT INTO area VALUE (?, ?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        preparedStatement.setInt(3, day);
        preparedStatement.setString(4, text);
        preparedStatement.executeUpdate();
    }

    public static void insertKangarooDetail( int recordID, int day, String text) throws SQLException {
        //get the kangaroo detail from the kangarooDetail table of second database
        Connection conn = conDB2();
        String sql = "INSERT INTO kangaroo VALUE (?, ?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        preparedStatement.setInt(3, day);
        preparedStatement.setString(4, text);
        preparedStatement.executeUpdate();

    }
    public static void insertSummary( int recordID,int day, String text) throws SQLException {
        //insert the summary record into the summaries table of first database
        Connection conn = conDB1();
        String sql = "INSERT INTO summaries VALUE (?, ?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        preparedStatement.setInt(3, day);
        preparedStatement.setString(4, text);
        preparedStatement.executeUpdate();

    }
    public static void insertPathAndJumping( int recordID, String jumping,String path) throws SQLException {
        //get the jumping history of the kangaroos from the summary table of second database
        Connection conn = conDB2();
        String sql = "INSERT INTO logs VALUE (?, ?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, LoginController.getUserName());
        preparedStatement.setInt(2, recordID);
        preparedStatement.setString(3, jumping);
        preparedStatement.setString(4, path);
        preparedStatement.executeUpdate();
    }
}
