package ru.projectMS.filesProject;


import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static ru.projectMS.connectionDB.ConnectionsDbPostGres.getConnection;

public class ProjectList {





    public class Projects{
        private int rnk;
        private  String projectName;
        private Timestamp modifiedDate;

        public Projects(int rnk, String projectName, Timestamp modifiedDate) {
            this.rnk = rnk;
            this.projectName = projectName;
            this.modifiedDate = modifiedDate;
        }

        } ;


        public Timestamp getList() {

            Timestamp timestamp;
            String project;
            {
                System.out.println("id; наименование проекта; дата изменения");
                String Query = "select distinct \n" +
                        "rank() over( partition by project_name order by modified_date) rnk,\n" +
                        " project_name, modified_date\n" +
                        "from public.project";

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

                System.out.println("Выберете документ (id проекта): ");
                Scanner scanner = new Scanner(System.in);
                int sel = scanner.nextInt();
                timestamp = projectsArrayList.get(sel).modifiedDate;
//                System.out.println(timestamp);
                project = projectsArrayList.get(sel).projectName;
            }
            return timestamp;
        }
}
