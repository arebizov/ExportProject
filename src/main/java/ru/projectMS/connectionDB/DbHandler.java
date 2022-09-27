package ru.projectMS.connectionDB;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.h2.jdbcx.JdbcConnectionPool;
import ru.projectMS.filesProject.ProjectMS;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import static ru.projectMS.connectionDB.ConnectionsDbH2.getConnectionPool;

public class DbHandler {
    private String files;
    private String filename;
    public String builders;
    public String Query;
    public static   Date mindate;


    public void initialize() throws SQLException {


        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
        Statement stmt;
        try {
            Connection connection = jdbcConnectionPool.getConnection();
            stmt = connection.createStatement();

            if (stmt != null) {
                System.out.println("You connected to DB");
            }
        } catch (Exception e) {
            System.out.println("Connection Failed :");
            e.printStackTrace();
        } finally {
            jdbcConnectionPool.dispose();
        }
    }

    ;


    public void insertDB(String resourseName, String taskName, int taskId, java.util.Date dateAssig, float val, String type, String taskGUID, java.util.Date monday, String resourcetype, String builder, String typeWork, String actFinish, String start, String finish, String project_name, String materiallabel) throws SQLException {


        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
        String Query = "INSERT INTO PROJECT (RESOURSENAME, taskName, taskId, dateAssig, val, type, taskguid, monday, resourceType, builder, typeWork, actFinish, start,finish , project_name, materiallabel) values (?,?,?,?,?,?,?,?, ?,?,?, ?, ? , ?, ?, ?)";
        try (Connection connection = jdbcConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, resourseName);
            preparedStatement.setString(2, taskName);
            preparedStatement.setInt(3, taskId);
            preparedStatement.setDate(4, (Date) dateAssig);
            preparedStatement.setDouble(5, val);
            preparedStatement.setString(6, type);
            preparedStatement.setString(7, taskGUID);
            preparedStatement.setDate(8, (Date) monday);
            preparedStatement.setString(9, resourcetype);
            preparedStatement.setString(10, builder);
            preparedStatement.setString(11, typeWork);
            preparedStatement.setString(12, actFinish);
            preparedStatement.setString(13, start);
            preparedStatement.setString(14, finish);
            preparedStatement.setString(15, project_name);
            preparedStatement.setString(16, materiallabel);
            preparedStatement.execute();
            connection.commit();
        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            jdbcConnectionPool.dispose();
        }
    }


    public void insertSumTask(String taskName, int taskId, String type, String typeWork, String finish, String sumTask, String projectName, double val) throws SQLException {


        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
        String Query = "INSERT INTO PROJECT (taskName, taskId,  type, typeWork, actfinish, sum_task, project_name,TASKGUID, val) values (?,?,?,?,?,?,?,?,?)";
        try (Connection connection = jdbcConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, taskName);
            preparedStatement.setInt(2, taskId);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, typeWork);
            preparedStatement.setString(5, finish);
            preparedStatement.setString(6, sumTask);
            preparedStatement.setString(7, projectName);
            preparedStatement.setString(8, projectName);
            preparedStatement.setDouble(9, val);


            preparedStatement.execute();
            connection.commit();
        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            jdbcConnectionPool.dispose();
        }
    }


    public void insertAccum(java.util.Date start, java.util.Date finish, double value, String types, String taskName, String GUID, String resourceName, long period, int taskid, String resourceType, String builder, String typeWork, String actFinish, String materialLabel) throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
        String Query = "INSERT INTO assing (start, finish,  val, types,  taskName, GUID,  resourceName, period, taskid, resourceType, builder, typeWork,actFinish, materialLabel) values (?,?,?,?,?,?,?,?, ?,?,?,?,?,?)";
        try (Connection connection = jdbcConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query)) {
            connection.setAutoCommit(false);
            preparedStatement.setDate(1, (Date) start);
            preparedStatement.setDate(2, (Date) finish);
            preparedStatement.setDouble(3, (double) value);
            preparedStatement.setString(4, types);
            preparedStatement.setString(5, taskName);
            preparedStatement.setString(6, GUID);
            preparedStatement.setString(7, resourceName);
            preparedStatement.setLong(8, period);
            preparedStatement.setInt(9, taskid);
            preparedStatement.setString(10, resourceType);
            preparedStatement.setString(11, builder);
            preparedStatement.setString(12, typeWork);
            preparedStatement.setString(13, actFinish);
            preparedStatement.setString(14, materialLabel);

            preparedStatement.execute();
            connection.commit();
        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            jdbcConnectionPool.dispose();
        }
    }


    public void select() throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Выгрузка");
        int i = 0;
        try {
            String Query = "SELECT taskid,taskguid, taskname,  resoursename, monday, type, sum(val) val, resourceType  FROM PROJECT group by taskguid, resoursename, taskname, monday, type, taskid, resourceType";
            try (Connection connection = jdbcConnectionPool.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(Query)) {
                ResultSet rs = preparedStatement.executeQuery();

                Row row0 = sheet.createRow(0);
                Cell cell0 = row0.createCell(0);
                Cell cell01 = row0.createCell(1);
                Cell cell02 = row0.createCell(2);
                Cell cell03 = row0.createCell(3);
                Cell cell04 = row0.createCell(4);
                Cell cell05 = row0.createCell(5);
                Cell cell06 = row0.createCell(6);
                Cell cell07 = row0.createCell(7);


                cell0.setCellValue((String) "taskguid");
                cell01.setCellValue((String) "taskId");
                cell02.setCellValue((String) "taskName");
                cell03.setCellValue((String) "monday");
                cell04.setCellValue((String) "val");
                cell05.setCellValue((String) "type");
                cell06.setCellValue((String) "resoursename");
                cell07.setCellValue((String) "resourceType");


                while (rs.next()) {
                    i = i + 1;

                    String taskguid = rs.getString("taskguid");
                    int taskId = rs.getInt("taskId");
                    String taskName = rs.getString("taskName");
                    String monday = rs.getString("monday");
                    double val = rs.getDouble("val");
                    String types = rs.getString("type");
                    String resoursename = rs.getString("resoursename");
                    String resourceType = rs.getString("resourceType");


                    Row row = sheet.createRow(i);
                    Cell cell = row.createCell(0);
                    Cell cell1 = row.createCell(1);
                    Cell cell2 = row.createCell(2);
                    Cell cell3 = row.createCell(3);
                    Cell cell4 = row.createCell(4);
                    Cell cell5 = row.createCell(5);
                    Cell cell6 = row.createCell(6);
                    Cell cell7 = row.createCell(7);
                    cell.setCellValue((String) taskguid);
                    cell1.setCellValue((int) taskId);
                    cell2.setCellValue((String) taskName);
                    cell3.setCellValue((String) monday);
                    cell4.setCellValue((double) val);
                    cell5.setCellValue((String) types);
                    cell6.setCellValue((String) resoursename);
                    cell7.setCellValue((String) resourceType);

                }
            }

            try {
                ProjectMS projectMS = new ProjectMS();
                System.out.println(projectMS.getFilename());

                String file_name = projectMS.getFilename();

                FileOutputStream outputStream = new FileOutputStream(file_name);
                workbook.write(outputStream);
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Done");


        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            jdbcConnectionPool.dispose();
        }
    }


    public void selectBuilder() throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Подрядчикам");
        sheet.createFreezePane(15,3);
        sheet.groupColumn(0,5);
        sheet.setRowGroupCollapsed(5, true);
        HashMap<Integer, String> listBuilders = new HashMap<>();
        HashMap<Integer, String> listWork = new HashMap<>();
        String arrayBuilders = "select  ROW_NUMBER ( )   \n" +
                "    OVER ( order BY builder  ) id, builder from (\n" +
                "SELECT distinct builder  FROM PROJECT where builder !='null' )";


        try (Connection connection = jdbcConnectionPool.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(arrayBuilders)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                listBuilders.put(rs.getInt("id"), rs.getString("builder"));
            }

        }


        int i = 0;
        try {

            int keyBuilder;
            System.out.println("Шаг 1 из 2");
            System.out.println("Выберете подрядчика");
            listBuilders.forEach((k, v) -> {
                System.out.format(" %s.  %s%n", k, v);
            });


            Scanner scanner = new Scanner(System.in);
//                builders = scanner.nextLine();
            keyBuilder = scanner.nextInt();
            builders = listBuilders.get(keyBuilder);
            System.out.println(builders);


            String arrayTypeWork = " select 0 id, 'Завершить выбор' typework union all select  ROW_NUMBER ( )   \n" +
                    "    OVER ( order BY TypeWork  ) id, TypeWork from (\n" +
                    "SELECT distinct TypeWork  FROM PROJECT where typework not like '%ГРУППА РАБ%'  and builder='" + builders + "')";


            System.out.println("Шаг 2 из 2");
            System.out.println("Выберете тип работ через Enter");

            try (Connection connection = jdbcConnectionPool.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(arrayTypeWork)) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    listWork.put(rs.getInt("id"), rs.getString("typeWork"));
                }

            }
            listWork.forEach((k, v) -> {
                System.out.format(" %s.  %s%n", k, v);
            });

            StringBuilder listWorks = new StringBuilder();

            int ii = -1;
            while (ii != 0) {
                Scanner sc = new Scanner(System.in);
                ii = sc.nextInt();
                listWorks.append("'" + listWork.get(ii) + "',");

            }
            String lineQuery0 = listWorks.toString().toLowerCase(Locale.ROOT);
            String lineQuery01 = lineQuery0.replace(",'завершить выбор',", ",'завершить выбор'").toLowerCase(Locale.ROOT);
            String lineQuery = lineQuery01.replace("'завершить выбор',", "");
//            System.out.println(lineQuery0);

//            System.out.println(lineQuery);

            String Query1 = "select  * from ( \n" +
                    "        select * from (  \n" +
                    "                            SELECT sum_task,dense_rank() over ( order by taskid) rnk,   \n" +
                    "                            taskid, taskname, resoursename, builder,typework,   sum(val) val ,  \n" +
                    "         type,  monday,  \n" +
                    "case \n" +
                    "when type = 'факт' then monday else start end start ,\n" +
                    "case \n" +
                    "when type = 'факт' then monday else finish end finish, materialLabel" +
                    "        FROM PROJECT   \n" +
                    "          where length(actfinish) =4 and ((sum_task='true') or( BUILDER =? ))       \n" +
                    "         group by taskid, taskname, resoursename, builder,typework , type , monday, start,   \n" +
                    "        finish,materialLabel ) order by rnk asc, type asc) \n" +
                    "\n" +
                    "\n";


            String QueryAll = "" +
                    "select * from (" +
                    "SELECT sum_task,dense_rank() over ( order by taskid) rnk, " +
                    "taskid, taskname, resoursename, builder,typework,   sum(val) val , (select min(monday)  from project) mindate,  type, \n" +
                    "case " +
                    "when monday is null then  (select min(monday) from project) " +
                    "else monday " +
                    "end monday, " +
                    "case " +
                    "when type = 'plan'  then  start " +
                    "else '' " +
                    "end start, " +
                    "  case " +
                    "when type = 'plan'  then  finish " +
                    "else '' " +
                    "end finish, " +
                    " case " +
                    " when monday is null then 0 " +
                    " else " +
                    " datediff(day,  (select min(monday) from project), monday )/7 end week FROM PROJECT " +
                    "  where length(actfinish) =4 and ((sum_task='true') or( BUILDER =? ))   " +
                    " group by taskid, taskname, resoursename, builder,typework, (select min(monday) from project) , type , monday, start, " +
                    "finish) order by rnk asc, type desc";


//            if (lineQuery.length() > 0) {
//                Query = Query1;
//            } else Query = Query1;
            Query = Query1;



            String QueryPeriod2 = "Select mindate, datediff(day, mindate,maxdate)/7 week " +
                    "from ( select min(monday) mindate,  max(monday) maxdate   from project  " +
                    "where builder =?)"
                    ;

            String QueryPeriod ="Select mindate, datediff(day, mindate,maxdate)/7 week " +
                    "      from ( select min(monday) mindate,  max(monday) maxdate   from project " +
                    "where length(actfinish) =4 and ((sum_task='true') or( BUILDER =?)) and lower(typework) in (" + lineQuery + ")) ";


            try (Connection connection = jdbcConnectionPool.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(QueryPeriod)) {
                preparedStatement.setString(1, builders);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int hatWeek = rs.getInt("week");
                    Date hatDates = rs.getDate("mindate");
                    mindate = hatDates;

                    Row row0 = sheet.createRow(0);
                    Row row1 = sheet.createRow(1);
                    Row row2 = sheet.createRow(2);


                    Cell row0Сell0 = row0.createCell(0);
                    Cell row0Сell1 = row0.createCell(1);
                    Cell row0Сell2 = row0.createCell(2);
                    Cell row0Сell3 = row0.createCell(3);
                    Cell row0Сell4 = row0.createCell(4);
                    Cell row0Сell5 = row0.createCell(5);
                    Cell row0Сell6 = row0.createCell(6);
                    Cell row0Сell7 = row0.createCell(7);
                    Cell row0Сell8 = row0.createCell(8);
                    Cell row0Сell9 = row0.createCell(9);
                    Cell row0Сell10 = row0.createCell(10);
                    Cell row0Сell11 = row0.createCell(11);
                    Cell row0Сell12 = row0.createCell(12);
                    Cell row0Сell13 = row0.createCell(13);
                    Cell row0Сell14 = row0.createCell(14);



                    Cell fact = row1.createCell(14);
                    fact.setCellValue("Факт");



                    row0Сell0.setCellValue((String) "Оставить");
                    row0Сell1.setCellValue((String) "Подрядчик");
                    row0Сell2.setCellValue((String) "Тип работ");
                    row0Сell3.setCellValue((String) "Суммарная задача");
                    row0Сell4.setCellValue((String) "Ресурс");
                    row0Сell5.setCellValue((String) "TaskID");
                    row0Сell6.setCellValue((String) "Задача");
                    row0Сell7.setCellValue((String) "Всего по смете");
                    row0Сell8.setCellValue((String) "План на");
                    row0Сell9.setCellValue((String) "Факт");
                    row0Сell10.setCellValue((String) "Остаток");
                    row0Сell11.setCellValue((String) "Базовое начало");
                    row0Сell12.setCellValue((String) "Базовое окончание");
                    row0Сell14.setCellValue((String) "План");
                    row0Сell13.setCellValue((String) "Ед. измер.");


                    Font font = workbook.createFont();
                    font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                    CellStyle cellStyleBlack = workbook.createCellStyle();
                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    cellStyleBlack.setFillForegroundColor(IndexedColors.BLACK.index);
                    cellStyleBlack.setFont(font);
                    cellStyleBlack.setAlignment(HorizontalAlignment.CENTER);




                    cellStyleBlack.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setDataFormat(
                            createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
                    cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                    CellStyle cellStyleGreen = workbook.createCellStyle();
                    cellStyleGreen.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);
                    cellStyleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyleGreen.setVerticalAlignment(VerticalAlignment.CENTER);

                    row0Сell0.setCellStyle(cellStyleGreen);
                    row0Сell1.setCellStyle(cellStyleGreen);
                    row0Сell2.setCellStyle(cellStyleGreen);
                    row0Сell3.setCellStyle(cellStyleGreen);
                    row0Сell4.setCellStyle(cellStyleGreen);
                    row0Сell5.setCellStyle(cellStyleGreen);
                    row0Сell6.setCellStyle(cellStyleGreen);
                    row0Сell7.setCellStyle(cellStyleGreen);
                    row0Сell8.setCellStyle(cellStyleGreen);
                    row0Сell9.setCellStyle(cellStyleGreen);
                    row0Сell10.setCellStyle(cellStyleGreen);
                    row0Сell11.setCellStyle(cellStyleGreen);
                    row0Сell12.setCellStyle(cellStyleGreen);
                    row0Сell13.setCellStyle(cellStyleGreen);
                    row0Сell14.setCellStyle(cellStyleGreen);

                    for (int a = 0; a <= hatWeek; a++) {

                        Calendar c = Calendar.getInstance();
                        Calendar e = Calendar.getInstance();
                        Calendar p = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

                        c.setTime(hatDates);
                        c.add(Calendar.DATE, a * 7);
                        Cell c_start = row0.createCell(15 + a);
                        c_start.setCellValue(c.getTime());
                        e.setTime(hatDates);
                        e.add(Calendar.DATE, a * 7 + 6);
                        Cell c_end = row1.createCell(15 + a);
                        c_end.setCellValue(e.getTime());
                        Cell c_plus = row2.createCell(15 + a);
                        String form = "IF(OFFSET(P2,0," + a + ",1,1)<=I2,\"+\",\"-\")";
                        c_plus.setCellFormula(form);
                        c_plus.setCellStyle(cellStyleBlack);
                        c_end.setCellStyle(cellStyle);
                        c_start.setCellStyle(cellStyle);
                        Cell dates = row1.createCell(8);
                        String string = "01.01.2020";
                        SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
                        p.setTime(df1.parse(string));
                        Cell cell1 = row1.createCell(1);
                        Cell cell2 = row1.createCell(2);
                        Cell cell3 = row1.createCell(3);
                        Cell cell4 = row1.createCell(4);

                        dates.setCellValue(p.getTime());
                        dates.setCellStyle(cellStyle);

                    }
                    for (int a = 0; a < 15; a++) {
                        Cell c_date = row2.createCell(a);
                        c_date.setCellStyle(cellStyleBlack);
                    }


                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            for (int h = 0; h<8;h++) {
                sheet.addMergedRegion(new CellRangeAddress(0, 1, h, h));
            }

            for (int h = 9; h<=14;h++) {
                sheet.addMergedRegion(new CellRangeAddress(0, 1, h, h));
            }

            try (Connection connection = jdbcConnectionPool.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(Query)) {

                System.out.println(builders);

                preparedStatement.setString(1, builders);
                ResultSet rs = preparedStatement.executeQuery();

                CellStyle cellStyleBlue = workbook.createCellStyle();
                cellStyleBlue.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
                cellStyleBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyleBlue.setVerticalAlignment(VerticalAlignment.CENTER);

                CellStyle cellStyleHat = workbook.createCellStyle();
                CellStyle cellStyleWhiteString = workbook.createCellStyle();


                cellStyleHat.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);
                cellStyleHat.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyleHat.setAlignment(HorizontalAlignment.CENTER);
                cellStyleHat.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyleHat.setWrapText(true);


                cellStyleWhiteString.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyleWhiteString.setWrapText(true);







                while (rs.next()) {
                    int j = rs.getInt("rnk");
                    String type = rs.getString("type");
                    if (type.equals("план")) {
                        Row rowf = sheet.createRow(j * 2 - 1 + 3);
                        Row rowp = sheet.createRow(j * 2 - 1 + 2);

                    }

                }

                ResultSet rs2 = preparedStatement.executeQuery();




                CellStyle cellStyleRound = workbook.createCellStyle();
                DataFormat format = workbook.createDataFormat();
                String styleFormat ="0.00";
                cellStyleRound.setDataFormat(format.getFormat(styleFormat));

                CellStyle cellStyleRedFont = workbook.createCellStyle();
                Font redFont = workbook.createFont();
                redFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                cellStyleRedFont.setFont(redFont);
                cellStyleRedFont.setDataFormat(format.getFormat(styleFormat));


                while (rs2.next()) {
                    String mark = "+";
                    String builder = rs2.getString("builder");
                    String typeWork = rs2.getString("typeWork");
                    int taskId = rs2.getInt("taskId");
                    String taskName = rs2.getString("taskName");
                    String resourseName = rs2.getString("resourseName");
                    double val = rs2.getDouble("val");
                    String type = rs2.getString("type");
                    int rnk = rs2.getInt("rnk");
                    String sumTask = rs2.getString("Sum_task");
                    String startBaseline = rs2.getString("start");
                    String finishBaseline = rs2.getString("finish");
                    Date monday = rs2.getDate("monday");
                    int week = 0;
                    if (monday!=null){
                        week = (int)(((monday.getTime()-mindate.getTime()) / (1000 * 60 * 60 * 24))+1)/7;
                    }
//                    System.out.println(mindate + " "+ monday+ " "+ week);
                    String materialLabel = rs2.getString("materialLabel");

                    int k = 0;

                    if (type.equals("план")) {
                        k = rnk * 2 - 1 + 2;
                    }
                    if (type.equals("факт")) {
                        k = rnk * 2 + 2;
                    }

                    Row rowPr = sheet.getRow(k-1);
                    Row row = sheet.getRow(k);
                    Row rowfact = sheet.getRow(k);
                    int numberStr = k + 1;
                    int numberStrPre = k;
                    Cell c_mark = row.createCell(0);
                    Cell c_builder = row.createCell(1);
                    Cell c_typeWork = row.createCell(2);
                    Cell c_sumTask = row.createCell(3);
                    Cell c_resourseName = row.createCell(4);
                    Cell c_taskId = row.createCell(5);
                    Cell c_taskName = row.createCell(6);
                    Cell c_sumPlan = row.createCell(7);
                    Cell c_planDate = row.createCell(8);

                    Cell c_sumFact = rowPr.createCell(9);
                    Cell c_balance = row.createCell(10);
                    Cell c_startBaseline = row.createCell(11);
                    Cell c_finishBaseline = row.createCell(12);
                    Cell c_type = row.createCell(14);
                    Cell c_materialLabel = row.createCell(13);

                    Cell c_val = row.createCell(15 + week);
                    c_val.setCellStyle(cellStyleRound);

                    for (int a=1; a <14; a++) {
                        try {
                            sheet.addMergedRegion(new CellRangeAddress(k, k + 1, a, a));
                        } catch (Exception e) {

                        }
                    }


                    c_builder.setCellValue((String) builder);
                    c_typeWork.setCellValue((String) typeWork);
                    c_resourseName.setCellValue((String) resourseName);
                    c_taskId.setCellValue((int) taskId);
                    c_taskName.setCellValue((String) taskName);
                    c_materialLabel.setCellValue(materialLabel);




                    if (val != 0 && sumTask ==null ) {
                            c_val.setCellValue(val);
                            c_val.setCellStyle(cellStyleRound);

                    }


                    c_type.setCellValue((String) type);

                    c_mark.setCellValue(mark);

                    if (k % 2 != 0) {
                        int j = k + 1;


                        if (sumTask == null) {
                            c_planDate.setCellFormula("SUMIF($O$3:$PPP$3,\"+\",O" + j + ":PPP" + j + ")");
                            c_balance.setCellFormula("H" + j + "-" + "J" + j);
                        }

                    }

                    if (type.equals("план") ){
                        c_startBaseline.setCellValue(startBaseline);
                        c_finishBaseline.setCellValue(finishBaseline);
                    }

                    c_sumTask.setCellValue((String) sumTask);

                    if (type.equals("план") && sumTask == null) {
                        c_sumPlan.setCellFormula("sum(O" + numberStr + ":ppp" + numberStr + ")");

                    }
                    if (type.equals("факт") && sumTask == null) {
                        c_sumFact.setCellFormula("sum(O" + numberStr + ":ppp" + numberStr + ")");
                        c_val.setCellStyle(cellStyleRedFont);
                    }

                    if (type.equals("факт")) {
                        c_type.setCellStyle(cellStyleBlue);

                    }

                    if (sumTask !=null) {
                        c_sumTask.setCellStyle(cellStyleBlue);
                        c_typeWork.setCellStyle(cellStyleBlue);
                        c_taskName.setCellStyle(cellStyleBlue);
                        c_taskId.setCellStyle(cellStyleBlue);
                        c_resourseName.setCellStyle(cellStyleBlue);
                        c_builder.setCellStyle(cellStyleBlue);
                        c_startBaseline.setCellStyle(cellStyleBlue);
                        c_finishBaseline.setCellStyle(cellStyleBlue);
                        c_sumPlan.setCellStyle(cellStyleBlue);
                        c_planDate.setCellStyle(cellStyleBlue);
                        c_balance.setCellStyle(cellStyleBlue);
                        c_sumFact.setCellStyle(cellStyleBlue);
                        c_materialLabel.setCellStyle(cellStyleBlue);
                    }

                }

                CellStyle cellStyleBlack = workbook.createCellStyle();
                cellStyleBlack.setFillForegroundColor(IndexedColors.BLACK.index);
                cellStyleBlack.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyleBlack.setVerticalAlignment(VerticalAlignment.CENTER);


                Row row2 = sheet.getRow(2);
                 Cell cell = row2.getCell(9);
                 cell.setCellStyle(cellStyleBlack);
                for (int p = 14; p < 120; p++) {

                    sheet.autoSizeColumn(p);
                }
                sheet.setColumnWidth(0, 2500);
                sheet.setColumnWidth(1, 3000);
                sheet.setColumnWidth(2, 3000);
                sheet.setColumnWidth(3, 3000);
                sheet.setColumnWidth(4, 4000);
                sheet.setColumnWidth(5, 2000);
                sheet.setColumnWidth(6, 13000);
                sheet.setColumnWidth(7, 3000);
                sheet.setColumnWidth(8, 3000);
                sheet.setColumnWidth(9, 3000);
                sheet.setColumnWidth(10, 3000);
                sheet.setColumnWidth(11, 3000);
                sheet.setColumnWidth(12, 3000);
                sheet.setAutoFilter(CellRangeAddress.valueOf("A3:PPP3"));

            }


            try {
                ProjectMS projectMS = new ProjectMS();
                System.out.println(projectMS.getFilename());

                String file_name = projectMS.getFilename();

                String file_name_new = file_name.replace(".xlsx", builders + "_подрядчикам.xlsx");

                FileOutputStream outputStream = new FileOutputStream(file_name_new);
                workbook.write(outputStream);
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Done");


        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            jdbcConnectionPool.dispose();
        }
    }


    public void selectAssign() throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();

        try {

            String Query1 = "SELECT start,val/period perDay, types,taskname,guid,resourcename, period, taskid, resourcetype,  builder, typeWork, actFinish, start, finish, materiallabel FROM ASSING  ";


            try (Connection connection = jdbcConnectionPool.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(Query1)) {
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {

                    int period = rs.getInt("period");
                    for (int j = 0; j < period; j++) {
                        Date days = rs.getDate("start");
                        Calendar c = Calendar.getInstance();
                        c.setTime(days);
                        c.add(Calendar.DAY_OF_MONTH, j);
                        Calendar c2 = Calendar.getInstance();
                        c2.setTime(days);
                        c2.add(Calendar.DAY_OF_MONTH, j - 1);

                        int monday = c2.get(Calendar.DAY_OF_WEEK);
                        c2.add(Calendar.DAY_OF_MONTH, -monday + 2);

                        float perDay = rs.getFloat("perDay");
                        String types = rs.getString("types");
                        String taskname = rs.getString("taskname");
                        String guid = rs.getString("guid");
                        String resourcename = rs.getString("resourcename");
                        int taskid = rs.getInt("taskid");
                        String resourcetype = rs.getString("resourcetype");
                        String builder = rs.getString("builder");
                        String typeWork = rs.getString("typeWork");
                        String actFinish = rs.getString("actFinish");
                        String start = rs.getString("start");
                        String finish = rs.getString("finish");
                        String project_name = ProjectMS.getProjectName();
                        String materiallabel = rs.getString("materiallabel");


                        java.util.Date utilDate = c.getTime();
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                        java.util.Date utilDate2 = c2.getTime();
                        java.sql.Date sqlDate2 = new java.sql.Date(utilDate2.getTime());

                        insertDB(resourcename, taskname, taskid, sqlDate, perDay, types, guid, sqlDate2, resourcetype, builder, typeWork, actFinish, start, finish, project_name, materiallabel);


//                        System.out.println(c.getTime() + " " + c2.getTime() + " " + perDay + " " + types + " " + taskname + " " + guid + " " + resourcename + " " + taskid);

                    }


                }

            }

        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            jdbcConnectionPool.dispose();
        }
    }


    public void createTableProject() throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
        Connection connection = jdbcConnectionPool.getConnection();
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            stmt.execute("CREATE TABLE PROJECT ( RESOURSENAME varchar(255), taskId int, taskName varchar(255) ,dateAssig date, val float, type varchar(50), taskguid varchar(50), monday date, resourcetype varchar(50),  builder varchar(250), typeWork varchar(250), actFinish varchar(50), sum_task varchar(10), start varchar(50), finish varchar(50), project_name varchar(255), materiallabel varchar(255)  )");
            connection.commit();
        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jdbcConnectionPool.dispose();
        }
    }

    public void createTableAssign() throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
        Connection connection = jdbcConnectionPool.getConnection();
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            stmt.execute("CREATE TABLE assing (start date, finish date,  val float , types varchar(255),  taskName varchar(255), GUID varchar(255),  resourceName varchar(255), period int, taskid int, resourceType varchar(255),  builder varchar(250), typeWork varchar(250), actFinish varchar(50), materialLabel varchar(100))");
            connection.commit();
        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jdbcConnectionPool.dispose();
        }
    }

    public void dropTable() throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = getConnectionPool();
        Connection connection = jdbcConnectionPool.getConnection();
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            stmt.execute("DROP TABLE  if exists project; DROP TABLE  if exists assing ");
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jdbcConnectionPool.dispose();
        }
    }


}
