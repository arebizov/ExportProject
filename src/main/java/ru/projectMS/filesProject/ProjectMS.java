package ru.projectMS.filesProject;

import net.sf.mpxj.*;
import net.sf.mpxj.common.DefaultTimephasedWorkContainer;
import net.sf.mpxj.reader.UniversalProjectReader;
import ru.projectMS.connectionDB.AssignDAO;
import ru.projectMS.connectionDB.DbHandler;
import ru.projectMS.connectionDB.ProjectDataDAO;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static ru.projectMS.filesProject.FileDate.lastModified;

public class ProjectMS {

    public static String outFiles;
    public static String projectName;
    public static int action;
    public static Timestamp modifiedDate;

    public String getFilename() {

        return outFiles;
    }

//    public static String getProjectName() {
//
//        return projectName;
//    }

    public static void main(String[] args) throws Exception {


        System.out.println("Параметры заполнения файла:");
        System.out.println("1. В нулевой задачи должно быть проставлено имя проекта из текстового справочника код 30");
        System.out.println("2. В суммарных задачах Группа работ из кодировки справочника 2");
        System.out.println("3. В задачах (не суммарных) проставить подрядчика из кодировки справочника 1");
        System.out.println("4. Для отчёта по физ. объёмам в задачах в корпоративных полях проставить:");
        System.out.println("   - Тип СМР");
        System.out.println("   - Объект реализации");
        System.out.println();

        System.out.println("Выберете Ваши действия:");

        System.out.println("1. Просмотреть ранее загруженные проекты и сформировать отчёт на основании загруженных документов");
        System.out.println("2. Загрузить новый документ");
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();


        if (k == 1) {

            ProjectList projectList = new ProjectList();


            Timestamp t = projectList.getList();
            System.out.println(t);
            DbHandler dbHandler = new DbHandler();
            dbHandler.select(t);
            dbHandler.selectBuilder(t);


        } else {
            try {
                if (args.length == 0) {
                    System.out.println("Введите путь до файла MS Project");


//                    String filename = "d:\\onest.mpp"; // эту комментировать
                        String filename = "d:\\example2.mpp"; // эту комментировать
//                    String filename = "d:\\vr5.mpp"; // эту комментировать


//                    Scanner sc = new Scanner(System.in);
//                    String path = sc.nextLine();
//                    String filename1 = path.replace(".mpp", "");
//                    String filename = filename1 + ".mpp";
                    ProjectFile mpx = new UniversalProjectReader().read(filename);
                    projectName = getProjectName(mpx);
                    if (getProjectName(mpx) == null) {
                        System.out.println("Заполните имя проекта в локальном текстовом справочнике код 30");
                        System.out.println("загрузка документа невозможна");
                    } else {
                        Date fileModifiedDate = lastModified(filename);
                        modifiedDate = (Timestamp) fileModifiedDate;
                        outFiles = filename.replace("mpp", "xlsx");
                        ProjectDataDAO projectDataDAO = new ProjectDataDAO();
                        projectDataDAO.deleteProject(modifiedDate);
                        AssignDAO assignDAO = new AssignDAO();
                        assignDAO.deleteAssignment(modifiedDate);
                        listTasks(mpx);
                        listAssignments(mpx);
                    }

                } else {

                    String s = args[0];
                    outFiles = args[0].replace("mpp", "xlsx");
                    System.out.println("Путь к файлу " + s);

                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }

        }

    }


    private static void listAssignments(ProjectFile file) throws SQLException, NoSuchFieldException, IllegalAccessException {
        Task task;
        System.out.println();

        System.out.println("Reading assignment ");
        for (ResourceAssignment assignment : file.getResourceAssignments()) {
            task = assignment.getTask();

            if (task != null) {

                listTimephasedWork(assignment);
            }

        }
        System.out.println();


    }

    private static String getProjectName(ProjectFile file) {
        projectName = file.getTasks().get(0).getText(30);
        return projectName;
    }


    private static void listTasks(ProjectFile file) throws SQLException {

        String typeFact = "факт";

        String sumTask = "true";
        System.out.println("Reading tasks ");
        float val = 0;

        ProjectDataDAO projectDataDAO = new ProjectDataDAO();

        for (Task task : file.getTasks()) {

            if (task.getSummary() == true && task.getID() != 0 && task.getOutlineCode(2) != null) {
                projectDataDAO.insertProject(null, task.getID(), task.getName(), null, val, projectName, null, null, null, task.getOutlineCode(2), null, sumTask, null, null, projectName, null, modifiedDate, typeFact);

            }
        }
    }


    private static void listTimephasedWork(ResourceAssignment assignment) throws SQLException, NoSuchFieldException, IllegalAccessException {

        AssignDAO assignDAO = new AssignDAO();
        Task task = assignment.getTask();
        int days = (int) ((task.getFinish().getTime() - task.getStart().getTime()) / (1000 * 60 * 60 * 24)) + 1;
        if (days > 1) {

            Field field = assignment.getClass().getDeclaredField("m_timephasedActualWork");
            field.setAccessible(true);
            DefaultTimephasedWorkContainer timephasedActualWork = (DefaultTimephasedWorkContainer) field.get(assignment);

            Field m_data = timephasedActualWork.getClass().getDeclaredField("m_data");
            m_data.setAccessible(true);
            List timePhased = (List) m_data.get(timephasedActualWork);
            String types = "факт";
            String taskName = String.valueOf(assignment.getTask().getName());
            String GUID = projectName;
            String builder = String.valueOf(assignment.getTask().getOutlineCode(1));
            String typeWork = String.valueOf(assignment.getTask().getOutlineCode(2));
            String actFinish = String.valueOf(assignment.getTask().getActualFinish());
            Date actFinishDate = assignment.getTask().getActualFinish();
            String materialLabel = "нет";
            String resourceName;
            String resourceType;
            if (assignment.getResource() != null) {
                resourceName = String.valueOf(assignment.getResource().getName());
                resourceType = String.valueOf(assignment.getResource().getType());
                materialLabel = String.valueOf(assignment.getResource().getMaterialLabel());
            } else {
                resourceName = "";
                resourceType = "";
            }
            int taskid = assignment.getTask().getID();

            Calendar c = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            for (int i = 0; i < timePhased.size(); i++) {
                Date start = ((TimephasedWork) timePhased.get(i)).getStart();
                Date finish = ((TimephasedWork) timePhased.get(i)).getFinish();
                long period = (finish.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1;
                int periodInt = (int) period;
                String val = String.valueOf(((TimephasedWork) timePhased.get(i)).getTotalAmount());
                int a = val.indexOf("m");

                Double value;
                if (a >= 0) {
                    value = Double.parseDouble(val.replace("m", "")) / 60;
                } else
                    value = Double.parseDouble(val.replace("h", ""));

                Float valFloat = value.floatValue();

                c.setTime(start);
                java.util.Date utilDate = c.getTime();
                java.sql.Date startDate = new java.sql.Date(utilDate.getTime());


                c2.setTime(finish);
                java.util.Date utilDate2 = c2.getTime();
                java.sql.Date finishDate = new java.sql.Date(utilDate2.getTime());
                System.out.println(startDate + " " + finishDate + " " + value + " " + types + " " + GUID + " " + builder + " " + typeWork + " " + actFinish + " " + materialLabel + " " + resourceName + " " + resourceType);
//                dbHandler.insertAccum( startDate, finishDate, value, types, taskName, GUID, resourceName, period, taskid, resourceType, builder, typeWork, actFinish, materialLabel, modifiedDate);
                assignDAO.insertAssignment(startDate, finishDate, valFloat, types, taskName, GUID, resourceName, periodInt, taskid, resourceType, builder, typeWork, actFinishDate, materialLabel, modifiedDate);
                assignDAO.normalizeInsertAssignment(startDate, finishDate, valFloat, types, taskName, GUID, resourceName, periodInt, taskid, resourceType, builder, typeWork, actFinishDate, materialLabel, modifiedDate);
            }
        }

        if (assignment.getBaselineStart() != null & assignment.getResource() != null) {
            Date start = assignment.getBaselineStart();
            Date finish = assignment.getBaselineFinish();
            long period = (finish.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1;
            int periodInt = (int) period;
            Calendar c = Calendar.getInstance();
            c.setTime(start);
            java.util.Date utilDate = c.getTime();
            java.sql.Date startDate = new java.sql.Date(utilDate.getTime());

            Calendar c2 = Calendar.getInstance();
            c2.setTime(finish);
            java.util.Date utilDate2 = c2.getTime();
            java.sql.Date finishDate = new java.sql.Date(utilDate2.getTime());


            String v = String.valueOf(assignment.getWork());
            double value = Double.parseDouble(v.replace("h", ""));
            float valFloat = (float) value;
            String types = "план";
            String typesFact = "факт";
            String taskName = String.valueOf(assignment.getTask().getName());
            String GUID = projectName;
            String resourceName = String.valueOf(assignment.getResource().getName());
            int taskid = assignment.getTask().getID();
            String resourceType = (String.valueOf(assignment.getResource().getType()));
            String builder = String.valueOf(assignment.getTask().getOutlineCode(1));
            String typeWork = String.valueOf(assignment.getTask().getOutlineCode(2));
            String actFinish = String.valueOf(assignment.getTask().getActualFinish());
            Date actFinishDate = assignment.getTask().getActualFinish();
            String materialLabel = "нет";

            if (assignment.getResource() != null) {

                materialLabel = String.valueOf(assignment.getResource().getMaterialLabel());
            } else {
                materialLabel = "нет";
            }

            assignDAO.insertAssignment(startDate, finishDate, valFloat, types, taskName, GUID, resourceName, periodInt, taskid, resourceType, builder, typeWork, actFinishDate, materialLabel, modifiedDate);
            assignDAO.normalizeInsertAssignment(startDate, finishDate, valFloat, types, taskName, GUID, resourceName, periodInt, taskid, resourceType, builder, typeWork, actFinishDate, materialLabel, modifiedDate);

        }
    }
}


