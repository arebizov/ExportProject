package ru.projectMS.connectionDB;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.projectMS.model.Assignment;
import ru.projectMS.model.ProjectData;

import java.sql.Timestamp;
import java.util.Date;

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



}

