package ru.projectMS.connectionDB;

import ru.projectMS.model.ProjectServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.projectMS.connectionDB.ConnectionsDbMSSQL.getConnectionProjectServer;

public class ProjectServerDAO {
    public List<ProjectServer> selectProject(String projectName) {

        List<ProjectServer> list = new ArrayList<ProjectServer>();
        try {
            String Query = " select task_id, corp_cmr, corp_obj  from dbo.project_server where project_name = ?";

            try (Connection connection = getConnectionProjectServer();
                 PreparedStatement preparedStatement = connection.prepareStatement(Query)) {
                preparedStatement.setString(1,projectName);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int taskId = rs.getInt("task_id");
                    String corpCMR = rs.getString("corp_cmr");
                    String corpObj = rs.getString("corp_obj");

                    list.add(new ProjectServer(taskId,corpCMR,corpObj ));

                }

//                for (ProjectServer pr : list){
//                    System.out.println(pr.getCorpCmr());
//                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}