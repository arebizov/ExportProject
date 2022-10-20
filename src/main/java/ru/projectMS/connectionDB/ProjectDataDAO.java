package ru.projectMS.connectionDB;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.projectMS.model.ProjectData;
import ru.projectMS.model.ProjectServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.projectMS.connectionDB.ConnectionsDbMSSQL.getConnectionProjectServer;

public class ProjectDataDAO {


    public void insertProject(String resourceName, int taskID, String taskName, Date dateAssig, float val, String taskGuid, Date monday, String resourceType, String builder, String typeWork, Date actFinish, String sumTask, Date start, Date finish, String projectName, String materialLabel, Timestamp modifiedDate, String types) {
        {
            String plan = "план";
            Configuration configuration = new Configuration().addAnnotatedClass(ProjectData.class);
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.getCurrentSession();

            try {

                ProjectData projectData = new ProjectData(resourceName, taskID, taskName, dateAssig, val, taskGuid, monday, resourceType, builder, typeWork, actFinish, sumTask, start, finish, projectName, materialLabel, modifiedDate, types);

                ProjectData projectDataPlan = new ProjectData(resourceName, taskID, taskName, dateAssig, val, taskGuid, monday, resourceType, builder, typeWork, actFinish, sumTask, start, finish, projectName, materialLabel, modifiedDate, plan);

                session.beginTransaction();
                session.save(projectData);
                session.save(projectDataPlan);
                session.getTransaction().commit();

            } finally {
                session.close();
                sessionFactory.close();
            }
        }

    }

    public void deleteProject( Timestamp modified) {

        Configuration configuration = new Configuration().addAnnotatedClass(ProjectData.class);
        SessionFactory sessionFactory2 = configuration.buildSessionFactory();
        Session session2 = sessionFactory2.getCurrentSession();

        try {
            session2.beginTransaction();
            String hql = "delete ProjectData where modifiedDate = :asd";
            Query query = session2.createQuery(hql);
            query.setParameter("asd", modified);

            query.executeUpdate();

            session2.getTransaction().commit();

        } finally {
            session2.close();
            sessionFactory2.close();
        }
    }

    public  void update(String projectName) throws SQLException {
        ProjectServerDAO projectServerDAO = new ProjectServerDAO();

        List<ProjectServer> list = projectServerDAO.selectProject(projectName);
        System.out.println("___________________________________");
        for (ProjectServer pr : list) {
            System.out.println(pr.getId());
        }

        String Query = " insert into pbi_1c.tmp_project (task_id, corp_cmr, corp_obj , project_name) values (?,?,?,?)";
        String Query2 = " delete from  pbi_1c.tmp_project where project_name = ?";

        String Update = "MERGE into pbi_1c.project u \n" +
                "USING (select * from pbi_1c.tmp_project ) s \n" +
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
                    System.out.println(pr.getId());
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

