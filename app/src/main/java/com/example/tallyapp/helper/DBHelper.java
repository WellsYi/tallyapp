package com.example.tallyapp.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    private static String url = "jdbc:mysql://10.89.129.129:3306/userinfo";
    private static String user = "root";
    private static String password = "123456";


    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url, user, password);
        return  connection;
    }

    public static void close(Connection connection, PreparedStatement ps, ResultSet res) throws SQLException {
        connection.close();
        ps.close();
        res.close();

    }


}
