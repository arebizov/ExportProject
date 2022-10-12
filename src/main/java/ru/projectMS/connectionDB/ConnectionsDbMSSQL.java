package ru.projectMS.connectionDB;


import ru.projectMS.filesProject.SchemaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionsDbMSSQL {


    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost;databaseName=master";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "Supperpassword1@";


    // Create connection
    public static Connection getConnection() throws SQLException {
        SchemaDB schemaDB = new SchemaDB();
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        String url = schemaDB.getURLDb();
        connection = DriverManager.getConnection(url);


        return connection;
    }
}