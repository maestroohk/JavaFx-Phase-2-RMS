package kimbugwe;

import java.sql.*;

public class ConnectDb {
    static Connection conn = null;
    static String url = "jdbc:mysql://localhost:3306/records?allowMultiQueries=true";
    static String user = "root";
    static String pass = "";
    static Connection establishConnection(){
        try {
            conn = DriverManager.getConnection(url, user, pass);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
