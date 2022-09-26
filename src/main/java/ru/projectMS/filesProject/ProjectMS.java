package ru.projectMS.filesProject;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.reader.UniversalProjectReader;
import ru.projectMS.connectionDB.DbHandler;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ProjectMS {

    public static String outFiles;
    public static String projectName;
    public static int action;


    public String getFilename() {

        return outFiles;
    }


    public static String getProjectName() {

        return projectName;
    }


    public static void main(String[] args) throws Exception {


        System.out.println("Параметры заполнения файла:");
        System.out.println("1. В нулевой задачи должно быть проставлено имя проекта из текстового справочника код 30");
        System.out.println("2. В суммарных задачах Группа работ из кодировки справочника 2");
        System.out.println("3. В задачах (не суммарных) проставить подрядчика из кодировки справочника 1");
        System.out.println("4. Для отчёта по физ. объёмам в задачах в корпоративных полях проставить:");
        System.out.println("   - Тип СМР");
        System.out.println("   - Объект реализации");

        {
            try {
                if (args.length == 0) {
                    System.out.println("Веедите путь до файла MS Project");


//                    String filename = "d:\\onest.mpp"; // эту комментировать
//                    String filename = "d:\\example2.mpp"; // эту комментировать

                    Scanner sc = new Scanner(System.in);
                    String path = sc.nextLine();
                    String filename1 = path.replace(".mpp", "");
                    String filename = filename1 + ".mpp";


                    outFiles = filename.replace("mpp", "xlsx");
                    query(filename);
                } else {

                    String s = args[0];
                    outFiles = args[0].replace("mpp", "xlsx");
                    System.out.println("Путь к файлу " + s);
                    query(s);
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }

    }


    private static void query(String filename) throws Exception {


        ProjectFile mpx = new UniversalProjectReader().read(filename);

        DbHandler dbHandler = new DbHandler();

        int sel;
        do {
            System.out.println("");
            System.out.println("Сделайте Ваш выбор, выберете от 1 до 4");
            System.out.println("1. Выгрузить данные по ФО для PowerBI");
            System.out.println("2. Выгрузить отчётные формы для подрядчиков СМР");
            System.out.println("3. 1 и 2");
            System.out.println("4. Выход");
            Scanner select = new Scanner(System.in);
            action = select.nextInt();
            sel = action;

        } while (sel > 4 | sel == 0);

        dbHandler.dropTable();

        dbHandler.createTableAssign();
        dbHandler.createTableProject();

        if (action > 0 & action < 4) {


            taskName0(mpx);
            listTasks(mpx);
            listAssignments(mpx);
        }


    }

    private static void listAssignments(ProjectFile file) throws SQLException {
        Task task;
        System.out.println();
//        System.out.println(file);
        DbHandler dbHandler = new DbHandler();

        System.out.println("Reading assignment ");
        for (ResourceAssignment assignment : file.getResourceAssignments()) {
            task = assignment.getTask();
            Z++;
            if (Z%10==0){
                System.out.print("\r");
            }
            else {
                System.out.print(".");
            }
            if (task != null) {

                listTimephasedWork(assignment);
            }

        }
        System.out.println();

        dbHandler.selectAssign();
        if (action == 1) {
            dbHandler.select();
        }

        if (action == 2) {

            while (true) {
                dbHandler.selectBuilder();
                System.out.println("Для продолжения выгрузки нажмите ENTER, для завершения наберите exit");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();

                if (s.equals("exit")) {
                    break;
                }
            }
        }


        if (action == 3) {
            dbHandler.select();
            while (true) {
                dbHandler.selectBuilder();
                System.out.println("Для продолжения выгрузки нажмите ENTER, для завершения наберите exit");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();

                if (s.equals("exit")) {
                    break;
                }
            }
        }

    }

    private static void taskName0(ProjectFile file) {

        for (Task task : file.getTasks()) {
            if (task.getID() == 0) {
                projectName = String.valueOf(task.getText(30));
                ;
            }

        }

    }

    public static int Z =0;

    private static void listTasks(ProjectFile file) throws SQLException {
        DbHandler dbHandler = new DbHandler();
        String typeFact = "факт";
        String typePlan = "план";
        String finish = "null";
        String sumTask = "true";
        System.out.println("Reading tasks ");


        for (Task task : file.getTasks()) {
            Z++;
//            System.out.print(Z);
//            System.out.println(task.getOutlineCode(2));

//            System.out.println((double)task.getPercentageComplete());
            if (Z % 10 == 0) {
                System.out.print("\r");
            } else {
                System.out.print(".");
            }



            if (task.getSummary() == true && task.getID() != 0 && task.getOutlineCode(2) != null) {
//                System.out.println(task.getOutlineCode(2));
                dbHandler.insertSumTask(task.getName(), task.getID(), typeFact, task.getOutlineCode(2), finish, sumTask, projectName, (double)task.getPercentageComplete() );
                dbHandler.insertSumTask(task.getName(), task.getID(), typePlan, task.getOutlineCode(2), finish, sumTask, projectName, (double)task.getPercentageComplete());
            }


        }


    }

    private static void listTimephasedWork(ResourceAssignment assignment) throws SQLException {

        DbHandler dbHandler = new DbHandler();
        Task task = assignment.getTask();
        int days = (int) ((task.getFinish().getTime() - task.getStart().getTime()) / (1000 * 60 * 60 * 24)) + 1;
        if (days > 1) {


            if (!assignment.getTimephasedActualWork().isEmpty()) {


                String resourceName;
                String resourceType;
                for (int i = 0; i < assignment.getTimephasedActualWork().size(); i++) {

                    Date start = assignment.getTimephasedActualWork().get(i).getStart();
                    Date finish = assignment.getTimephasedActualWork().get(i).getFinish();
                    long period = (finish.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1;

                    Calendar c = Calendar.getInstance();
                    c.setTime(start);
                    java.util.Date utilDate = c.getTime();
                    java.sql.Date startDate = new java.sql.Date(utilDate.getTime());

                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(finish);
                    java.util.Date utilDate2 = c2.getTime();
                    java.sql.Date finishDate = new java.sql.Date(utilDate2.getTime());


                    String v = String.valueOf(assignment.getTimephasedActualWork().get(i).getTotalAmount());
                    Double value = Double.parseDouble(v.replace("h", ""));
                    String types = "факт";
                    String taskName = String.valueOf(assignment.getTask().getName());
                    String GUID = ProjectMS.getProjectName();
                    String builder = String.valueOf(assignment.getTask().getOutlineCode(1));
                    String typeWork = String.valueOf(assignment.getTask().getOutlineCode(2));
                    String actFinish = String.valueOf(assignment.getTask().getActualFinish());
                    String materialLabel = "нет";

                    if (assignment.getResource() != null) {
                        resourceName = String.valueOf(assignment.getResource().getName());
                        resourceType = String.valueOf(assignment.getResource().getType());
                        materialLabel = String.valueOf(assignment.getResource().getMaterialLabel());
                    } else {
                        resourceName = "";
                        resourceType = "";
                    }
                    int taskid = assignment.getTask().getID();

                    dbHandler.insertAccum(startDate, finishDate, value, types, taskName, GUID, resourceName, period, taskid, resourceType, builder, typeWork, actFinish, materialLabel);

                }

            }

            if (assignment.getBaselineStart() != null & assignment.getResource() != null) {
                Date start = assignment.getBaselineStart();
                Date finish = assignment.getBaselineFinish();
                long period = (finish.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1;

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
                double valueNull = 0;
                String types = "план";
                String typesFact = "факт";
                String taskName = String.valueOf(assignment.getTask().getName());
                String GUID = ProjectMS.getProjectName();
                String resourceName = String.valueOf(assignment.getResource().getName());
                int taskid = assignment.getTask().getID();
                String resourceType = (String.valueOf(assignment.getResource().getType()));
                String builder = String.valueOf(assignment.getTask().getOutlineCode(1));
                String typeWork = String.valueOf(assignment.getTask().getOutlineCode(2));
                String actFinish = String.valueOf(assignment.getTask().getActualFinish());
                String materialLabel = "нет";

                if (assignment.getResource() != null) {

                    materialLabel = String.valueOf(assignment.getResource().getMaterialLabel());
                } else {
                    materialLabel = "нет";
                }



//                System.out.println(resourceType);
//                System.out.println(unit);
                dbHandler.insertAccum(startDate, finishDate, value, types, taskName, GUID, resourceName, period, taskid, resourceType, builder, typeWork, actFinish , materialLabel);
                dbHandler.insertAccum(startDate, finishDate, valueNull, typesFact, taskName, GUID, resourceName, period, taskid, resourceType, builder, typeWork, actFinish, materialLabel);

            }
        }
    }

}
