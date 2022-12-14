package ru.projectMS.filesProject;


import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static ru.projectMS.connectionDB.ConnectionsDbMSSQL.getConnection;

public class ProjectList {
    static Profile profile = new Profile();
    public static String schema = profile.getSchema() ;

    public class Projects {
        private int rnk;
        private String projectName;
        private Timestamp modifiedDate;

        public Projects(int rnk, String projectName, Timestamp modifiedDate) {
            this.rnk = rnk;
            this.projectName = projectName;
            this.modifiedDate = modifiedDate;
        }

    }


    public Timestamp getList() {

        Timestamp timestamp;

        {
            System.out.println("id; наименование проекта; дата изменения");
            String Query = "select  rank() over (partition by t. project_name order by t.modified_date desc ) rnk, * from (\n" +
                    "select distinct project_name , modified_date from"+schema+"project_aggt) t";

            ArrayList<Projects> projectsArrayList = new ArrayList<>();
            try (Connection connection = getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(Query)) {

                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {

                    int rnk = rs.getInt("rnk");
                    String projectName = rs.getString("project_name");
                    Timestamp modifiedDate = rs.getTimestamp("modified_date");
                    projectsArrayList.add(new Projects(rnk, projectName, modifiedDate));


                }
                for (int i = 0; i < projectsArrayList.size(); i++) {

                    System.out.println(i + " ;" + projectsArrayList.get(i).projectName + " ;" + projectsArrayList.get(i).modifiedDate);
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Выберите документ (id проекта): ");
            Scanner scanner = new Scanner(System.in);
            int sel = scanner.nextInt();
            timestamp = projectsArrayList.get(sel).modifiedDate;

        }
        return timestamp;
    }
}
