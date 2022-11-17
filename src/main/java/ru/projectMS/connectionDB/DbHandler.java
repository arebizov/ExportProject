package ru.projectMS.connectionDB;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.projectMS.ProjectMS;
import ru.projectMS.filesProject.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import static ru.projectMS.connectionDB.ConnectionsDbMSSQL.getConnection;

public class DbHandler {

    public String builders;
    public String Query;
    public static Date mindate;
    public static String name;
    static Profile profile = new Profile();
    public static String schema = profile.getSchema();


    public void select(Timestamp modifiedDate) throws SQLException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Выгрузка");
        int i = 0;
        try {
            String Query = "\n" +
                    "SELECT task_id,task_guid, task_name,  resource_name , monday, type, sum(val) val, resource_type, corp_cmr, corp_obj   FROM" + schema + "PROJECT where modified_Date = ? group by task_guid, resource_name, task_name, monday, type, task_id, resource_type, corp_cmr, corp_obj  ";
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(Query)) {

                preparedStatement.setTimestamp(1, modifiedDate);
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
                Cell cell08 = row0.createCell(8);
                Cell cell09 = row0.createCell(9);


                cell0.setCellValue("taskguid");
                cell01.setCellValue("taskId");
                cell02.setCellValue("taskName");
                cell03.setCellValue("monday");
                cell04.setCellValue("val");
                cell05.setCellValue("type");
                cell06.setCellValue("resourcsename");
                cell07.setCellValue("resourceType");
                cell08.setCellValue("corp_cmr");
                cell09.setCellValue("corp_obj");


                while (rs.next()) {
                    i = i + 1;

                    String taskguid = rs.getString("task_guid");
                    int taskId = rs.getInt("task_id");
                    String taskName = rs.getString("task_name");
                    String monday = rs.getString("monday");
                    double val = rs.getDouble("val");
                    String types = rs.getString("type");
                    String resoursename = rs.getString("resource_name");
                    String resourceType = rs.getString("resource_type");
                    name = rs.getString("task_guid");
                    String corpCmr = rs.getString("corp_cmr");
                    String corpObj = rs.getString("corp_obj");

                    Row row = sheet.createRow(i);
                    Cell cell = row.createCell(0);
                    Cell cell1 = row.createCell(1);
                    Cell cell2 = row.createCell(2);
                    Cell cell3 = row.createCell(3);
                    Cell cell4 = row.createCell(4);
                    Cell cell5 = row.createCell(5);
                    Cell cell6 = row.createCell(6);
                    Cell cell7 = row.createCell(7);
                    Cell cell8 = row.createCell(8);
                    Cell cell9 = row.createCell(9);
                    cell.setCellValue(taskguid);
                    cell1.setCellValue(taskId);
                    cell2.setCellValue(taskName);
                    cell3.setCellValue(monday);
                    cell4.setCellValue(val);
                    cell5.setCellValue(types);
                    cell6.setCellValue(resoursename);
                    cell7.setCellValue(resourceType);
                    cell8.setCellValue(corpCmr);
                    cell9.setCellValue(corpObj);


                }
            }

            try {
                ProjectMS projectMS = new ProjectMS();
                System.out.println(projectMS.getFilename());

                CodeSource codeSource = ProjectMS.class.getProtectionDomain().getCodeSource();
                File jarFile = new File(codeSource.getLocation().toURI().getPath());
                String jarDir = jarFile.getParentFile().getPath();

                String file_name = jarDir + "\\" + name + ".xlsx";
                System.out.println("Файл сохранён в директории " + file_name);

                FileOutputStream outputStream = new FileOutputStream(file_name);
                workbook.write(outputStream);
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Done");


        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            getConnection().close();
        }
    }


    public void selectBuilder(Timestamp t) throws SQLException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Подрядчикам");
        sheet.createFreezePane(15, 3);
        sheet.groupColumn(0, 5);
        sheet.setRowGroupCollapsed(5, true);
        HashMap<Integer, String> listBuilders = new HashMap<>();
        HashMap<Integer, String> listWork = new HashMap<>();
        String arrayBuilders = "select  ROW_NUMBER ( )    \n" +
                "            OVER ( order BY build.builder  ) id, build.builder from ( \n" +
                "        SELECT distinct builder  FROM " + schema + "PROJECT where builder !='null' and modified_date = ?) build";


        try (Connection connection = getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(arrayBuilders)) {
            preparedStatement.setTimestamp(1, t);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                listBuilders.put(rs.getInt("id"), rs.getString("builder"));
            }

        }

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


            String arrayTypeWork = " select 0 id, 'Завершить выбор' type_work union all select  ROW_NUMBER ( )   \n" +
                    "    OVER ( order BY t.type_work  ) id, t.type_work from (\n" +
                    "SELECT distinct type_work  FROM " + schema + "PROJECT where type_work not like '%ГРУППА РАБ%' and modified_date =? and builder='" + builders + "') t";


            System.out.println("Шаг 2 из 2");
            System.out.println("Выберете тип работ через Enter");

            try (Connection connection = getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(arrayTypeWork)) {
                preparedStatement.setTimestamp(1, t);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    listWork.put(rs.getInt("id"), rs.getString("type_work"));
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


            String Query1 = "     SELECT sum_task, dense_rank() over ( order by task_id) rnk, task_id, task_name, resource_name, builder,type_work,   sum(val) val ,   \n" +
                    "                 type,  monday, modified_date,   \n" +
                    "        \t\t case  \n" +
                    "        \t\t\t when type  = 'факт' then monday else \"start\" end \"start\" , \n" +
                    "        \t\t case  \n" +
                    "        \t\t\twhen type = 'факт' then monday else finish end finish, \n" +
                    "        \t\tmaterial_Label \n" +
                    "                FROM  pbi_1c.PROJECT \n" +
                    "\t\t\t\twhere \n" +
                    "\t\t\tmodified_date =? and type_work='ГРУППА РАБОТ'  \n" +
                    "\t\t\t\t and  sum_task='true' or (BUILDER =?  and type_work in(" + lineQuery + ") ) \n" +
                    "\t\t\t\tgroup by  task_id, task_name, resource_name, builder,type_work , type , monday,\"start\", finish, material_label,sum_task, modified_date\n" +
                    "\t\t\t\torder by rnk asc, type asc ";


            Query = Query1;


            String QueryPeriod = " select min(monday) mindate,  max(monday) maxdate   from " + schema + "project " +
                    "where act_finish is null and ((sum_task='true') or( BUILDER =?)) and lower(type_work) in (" + lineQuery + ")  ";


            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(QueryPeriod)) {
                preparedStatement.setString(1, builders);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Date start = rs.getDate("mindate");
                    Date finish = rs.getDate("maxdate");

                    long period = ((finish.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1) / 7;
                    int hatWeek = (int) period;
//                    int hatWeek = rs.getInt("week");
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


                    row0Сell0.setCellValue("Оставить");
                    row0Сell1.setCellValue("Подрядчик");
                    row0Сell2.setCellValue("Тип работ");
                    row0Сell3.setCellValue("Суммарная задача");
                    row0Сell4.setCellValue("Ресурс");
                    row0Сell5.setCellValue("TaskID");
                    row0Сell6.setCellValue("Задача");
                    row0Сell7.setCellValue("Всего по смете");
                    row0Сell8.setCellValue("План на");
                    row0Сell9.setCellValue("Факт");
                    row0Сell10.setCellValue("Остаток");
                    row0Сell11.setCellValue("Базовое начало");
                    row0Сell12.setCellValue("Базовое окончание");
                    row0Сell14.setCellValue("План");
                    row0Сell13.setCellValue("Ед. измер.");


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

            for (int h = 0; h < 8; h++) {
                sheet.addMergedRegion(new CellRangeAddress(0, 1, h, h));
            }

            for (int h = 9; h <= 14; h++) {
                sheet.addMergedRegion(new CellRangeAddress(0, 1, h, h));
            }

            try (Connection connection = getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(Query)) {

                System.out.println(builders);
                preparedStatement.setTimestamp(1, t);
                preparedStatement.setString(2, builders);

                System.out.println(t);
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
                String styleFormat = "0.00";
                cellStyleRound.setDataFormat(format.getFormat(styleFormat));

                CellStyle cellStyleRedFont = workbook.createCellStyle();
                Font redFont = workbook.createFont();
                redFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                cellStyleRedFont.setFont(redFont);
                cellStyleRedFont.setDataFormat(format.getFormat(styleFormat));

                int rnk = 0;

                while (rs2.next()) {
                    String mark = "+";
                    String builder = rs2.getString("builder");
                    String typeWork = rs2.getString("type_Work");
                    int taskId = rs2.getInt("task_Id");
                    String taskName = rs2.getString("task_Name");
                    String resourseName = rs2.getString("resource_Name");
                    double val = rs2.getDouble("val");
                    String type = rs2.getString("type");
                    rnk = rs2.getInt("rnk");
                    String sumTask = rs2.getString("Sum_task");
                    String startBaseline = rs2.getString("start");
                    String finishBaseline = rs2.getString("finish");
                    Date monday = rs2.getDate("monday");
                    int week = 0;
                    if (monday != null) {
                        week = (int) (((monday.getTime() - mindate.getTime()) / (1000 * 60 * 60 * 24)) + 1) / 7;
                    }
//                    System.out.println(mindate + " "+ monday+ " "+ week);
                    String materialLabel = rs2.getString("material_label");

                    int k = 0;

                    if (type.equals("план")) {
                        k = rnk * 2 - 1 + 2;
                    }
                    if (type.equals("факт")) {
                        k = rnk * 2 + 2;
                    }

                    Row rowPr = sheet.getRow(k - 1);
                    Row row = sheet.getRow(k);
                    Row rowfact = sheet.getRow(k);
                    int numberStr = k + 1;
//                    int numberStrPre = k;
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


                    c_builder.setCellValue(builder);
                    c_typeWork.setCellValue(typeWork);
                    c_resourseName.setCellValue(resourseName);
                    c_taskId.setCellValue(taskId);
                    c_taskName.setCellValue(taskName);
                    c_materialLabel.setCellValue(materialLabel);


                    if (val != 0 && sumTask.equals("false")) {
                        c_val.setCellValue(val);
                        c_val.setCellStyle(cellStyleRound);

                    }


                    c_type.setCellValue(type);

                    c_mark.setCellValue(mark);

                    if (k % 2 != 0) {
                        int j = k + 1;


                        if (sumTask.equals("false")) {
                            c_planDate.setCellFormula("SUMIF($O$3:$PPP$3,\"+\",O" + j + ":PPP" + j + ")");
                            c_balance.setCellFormula("H" + j + "-" + "J" + j);
                            c_planDate.setCellStyle(cellStyleRound);
                            c_balance.setCellStyle(cellStyleRound);
                        }

                    }

                    if (type.equals("план")) {
                        c_startBaseline.setCellValue(startBaseline);
                        c_finishBaseline.setCellValue(finishBaseline);
                    }

                    c_sumTask.setCellValue(sumTask);

                    if (type.equals("план") && sumTask.equals("false")) {
                        c_sumPlan.setCellFormula("sum(O" + numberStr + ":ppp" + numberStr + ")");
                        c_sumPlan.setCellStyle(cellStyleRound);

                    }
                    if (type.equals("факт") && sumTask.equals("false")) {
                        c_sumFact.setCellFormula("sum(O" + numberStr + ":ppp" + numberStr + ")");
                        c_sumFact.setCellStyle(cellStyleRound);
                        c_val.setCellStyle(cellStyleRedFont);
                    }

                    if (type.equals("факт")) {
                        c_type.setCellStyle(cellStyleBlue);

                    }

                    if (!sumTask.equals("false")) {
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

                for (int row = 3; row < rnk*2+3; row++) {
                    for (int a = 1; a < 14; a++) {
                        try {
                            sheet.addMergedRegion(new CellRangeAddress(row, row + 1, a, a));
                        } catch (Exception e) {

                        }
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

                CodeSource codeSource = ProjectMS.class.getProtectionDomain().getCodeSource();
                File jarFile = new File(codeSource.getLocation().toURI().getPath());
                String jarDir = jarFile.getParentFile().getPath();

                String file_name = jarDir + "\\" + name + "_подрядчикам.xlsx";
                System.out.println("Файл сохранён в директории " + file_name);

                FileOutputStream outputStream = new FileOutputStream(file_name);
                workbook.write(outputStream);
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Done");


        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            getConnection().close();
        }
    }

}
