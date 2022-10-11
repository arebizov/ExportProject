package ru.projectMS.connectionDB;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionsDbPostGres {


    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/postgres";
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
        String url = DB_CONNECTION+"?user="+DB_USER+"&password="+DB_PASSWORD;
        connection = DriverManager.getConnection(url);

        return connection;
    }
}
