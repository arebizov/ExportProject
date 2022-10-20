package ru.projectMS.connectionDB;


import ru.projectMS.filesProject.Profile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionsDbMSSQL {


    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static Connection getConnection() throws SQLException {
        Profile profile = new Profile();
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        String url = profile.getURLDb();
        connection = DriverManager.getConnection(url);

        return connection;
    }
    public static Connection getConnectionProjectServer() throws SQLException {
        Profile profile = new Profile();
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        String url = profile.getURLDbProjectServer();
        connection = DriverManager.getConnection(url);

        return connection;
    }


}