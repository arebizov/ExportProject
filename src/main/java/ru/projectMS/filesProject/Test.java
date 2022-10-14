package ru.projectMS.filesProject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.projectMS.connectionDB.ProjectServerDAO;
import ru.projectMS.model.ProjectData;
import ru.projectMS.model.ProjectServer;

import java.util.List;


public class Test {

    public static void main(String[] args) {

        ProjectServerDAO projectServerDAO = new ProjectServerDAO();
        List<ProjectServer>  list = projectServerDAO.selectProject();


//        for (ProjectServer pr : list){
//            System.out.println(pr.getCorpCmr());
//        }

        Configuration configuration = new Configuration().addAnnotatedClass(ProjectData.class);
        SessionFactory sessionFactory2 = configuration.buildSessionFactory();
        Session session2 = sessionFactory2.getCurrentSession();

        try {
            session2.beginTransaction();
            String hql = "from ProjectData ";
            Query query = session2.createQuery(hql);
            List<ProjectData>  listPoject = query.list();

            for(ProjectData l1 : listPoject){

                for (ProjectServer pr : list){
                    System.out.println(pr.getCorpObj());
                    if(l1.getTaskID()== pr.getId()){
                        l1.setCorpCMR(pr.getCorpCmr());
                    }
                }

            }

            session2.getTransaction().commit();

        } finally {
            session2.close();
            sessionFactory2.close();
        }

    }
}
