package ru.projectMS.filesProject;

public class SchemaDB {

    public String getSchema(){
        String shema = " dbo.";
        return shema;
    }

    public String getURLDb(){
        String url = "jdbc:sqlserver://localhost:1433;"+"database=master;" + "user=sa;"+"password=Supperpassword1@;";
//        String url = "jdbc:sqlserver://localhost:1433;"+"database=master;" + "user=sa;"+"password=Supperpassword1@;";
        return url;
    }
}
