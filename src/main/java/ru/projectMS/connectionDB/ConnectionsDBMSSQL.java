package ru.projectMS.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionsDBMSSQL {


    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";


    // Create connection
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        String url = "jdbc:sqlserver://MYPC\\SQLEXPRESS;databaseName=MYDB";
        connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);

        return connection;
    }
}
