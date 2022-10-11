package ru.projectMS.connectionDB;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.projectMS.model.Assignment;
import ru.projectMS.model.ProjectData;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class AssignDAO {


    public void insertAssignment(Date startDate, Date finishDate, float value, String types, String taskName, String GUID, String resourceName, int period, int taskid, String resourceType, String builder, String typeWork, Date actFinish, String materialLabel, Timestamp modifiedDate) {

        Configuration configuration = new Configuration().addAnnotatedClass(Assignment.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        float nul = 0;

        try {

            Assignment assignment = new Assignment(startDate, finishDate, value, types, taskName, GUID, resourceName, period, taskid, resourceType, builder, typeWork, actFinish, materialLabel, modifiedDate);
            Assignment assignmentNull = new Assignment(startDate, finishDate, nul, types, taskName, GUID, resourceName, period, taskid, resourceType, builder, typeWork, actFinish, materialLabel, modifiedDate);

            session.beginTransaction();
            session.save(assignment);
            session.save(assignmentNull);
            session.getTransaction().commit();

        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    public void normalizeInsertAssignment(Date startDate, Date finishDate, float value, String types, String taskName, String GUID, String resourceName, int period, int taskid, String resourceType, String builder, String typeWork, Date actFinish, String materialLabel, Timestamp modifiedDate) {

        String taskName2 = taskName;
        float value2 = value;
        Date start = startDate;
        String sumTask = "false";
        long periodAssign = (finishDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
        int periodInt = (int) periodAssign;
        float perDay = value / periodInt;
        float perDayNull = 0;
        Configuration configuration = new Configuration().addAnnotatedClass(ProjectData.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        try {
            for (int j = 0; j < periodInt; j++) {

                Calendar vStart = Calendar.getInstance();
                vStart.setTime(startDate);
                vStart.add(Calendar.DAY_OF_MONTH, j);
                Calendar vMonday = Calendar.getInstance();
                vMonday.setTime(startDate);
                vMonday.add(Calendar.DAY_OF_MONTH, j - 1);
                int monday = vMonday.get(Calendar.DAY_OF_WEEK);
                vMonday.add(Calendar.DAY_OF_MONTH, -monday + 2);

                ProjectData projectData = new ProjectData(resourceName, taskid, taskName, vStart.getTime(), perDay, GUID, vMonday.getTime(), resourceType, builder, typeWork, actFinish, sumTask, start, finishDate, GUID, materialLabel, modifiedDate, types);
                ProjectData projectDataNull = new ProjectData(resourceName, taskid, taskName, vStart.getTime(), perDayNull, GUID, vMonday.getTime(), resourceType, builder, typeWork, actFinish, sumTask, start, finishDate, GUID, materialLabel, modifiedDate, types);

                session.save(projectData);
                session.save(projectDataNull);

            }
            session.getTransaction().commit();
        } finally {
            session.close();
            sessionFactory.close();
        }

    }

    public void deleteAssignment(Timestamp modified) {

        Configuration configuration = new Configuration().addAnnotatedClass(Assignment.class);
        SessionFactory sessionFactory2 = configuration.buildSessionFactory();
        Session session2 = sessionFactory2.getCurrentSession();

        try {
            session2.beginTransaction();
            String hql = "delete Assignment where modified = :asd";
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
