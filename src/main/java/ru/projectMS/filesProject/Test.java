package ru.projectMS.filesProject;

import ru.projectMS.ProjectMS;
import ru.projectMS.model.ProjectServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.projectMS.connectionDB.ConnectionsDbMSSQL.getConnectionProjectServer;


public class Test {

    public static String projectName = "Врубеля, 4";

    public static void main(String[] args) throws SQLException {
        ProjectMS projectMS = new ProjectMS();

        map();
        update();

    }

    public static List<ProjectServer> list = new ArrayList<>();

    public static void map() {


        String Query = " select task_id, corp_cmr, corp_obj  from dbo.project_server where project_name = ?";

        try (Connection connection = getConnectionProjectServer();
             PreparedStatement preparedStatement = connection.prepareStatement(Query)) {
            preparedStatement.setString(1, projectName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                String corpCMR = rs.getString("corp_cmr");
                String corpObj = rs.getString("corp_obj");
                String projectName = rs.getString("corp_obj");

                list.add(new ProjectServer(taskId, corpCMR, corpObj,projectName ));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void update() throws SQLException {


        String Query = " insert into tmp_project (task_id, corp_cmr, corp_obj , project_name) values (?,?,?,?)";
        String Query2 = " delete from  tmp_project where project_name = ?";

        String Update = "MERGE into dbo.project u \n" +
                "USING (select * from tmp_project ) s \n" +
                "on u.task_id = s.task_id and u.project_name = s.project_name \n" +
                "WHEN  MATCHED THEN UPDATE \n" +
                "SET u.corp_cmr = s.corp_cmr , u.corp_obj = s.corp_obj; ";


        try (Connection connection = getConnectionProjectServer();

             PreparedStatement preparedStatement = connection.prepareStatement(Query2)) {
            preparedStatement.setString(1, projectName);

            preparedStatement.executeUpdate();


            try (

                    PreparedStatement preparedStatement2 = connection.prepareStatement(Query)) {


                for (ProjectServer pr : list) {
                    preparedStatement2.setInt(1, pr.getId());
                    preparedStatement2.setString(2, pr.getCorpCmr());
                    preparedStatement2.setString(3, pr.getCorpObj());
                    preparedStatement2.setString(4, projectName);
                    preparedStatement2.executeUpdate();

                }

                try (

                        PreparedStatement preparedStatement3 = connection.prepareStatement(Update)) {
                    preparedStatement3.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            } catch (
                    SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



