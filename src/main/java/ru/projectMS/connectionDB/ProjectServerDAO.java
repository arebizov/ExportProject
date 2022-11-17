package ru.projectMS.connectionDB;

import ru.projectMS.filesProject.Profile;
import ru.projectMS.model.ProjectServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.projectMS.connectionDB.ConnectionsDbMSSQL.getConnectionProjectServer;

public class ProjectServerDAO {
    Profile profile = new Profile();
    public List<ProjectServer> selectProject(String projectName) {

        List<ProjectServer> list = new ArrayList<ProjectServer>();


        try {
            //String Query = profile.getDataFromLocalProject(projectName);
            String Query = profile.getDataFromMSProject(projectName);

            try (Connection connection = getConnectionProjectServer();
                 PreparedStatement preparedStatement = connection.prepareStatement(Query)) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int taskId = rs.getInt("task_id");
                    String corpCMR = rs.getString("corp_cmr");
                    String corpObj = rs.getString("corp_obj");
                    list.add(new ProjectServer(taskId,corpCMR,corpObj, projectName ));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}