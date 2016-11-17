package ru.polynkina.irina.fileInteraction;

import ru.polynkina.irina.graphs.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.List;
import java.io.File;

public class LibraryEditor {

    private final  static int MAX_INDEX_MONTH = 12;

    // counter_...xls
    private final static int COL_INDICATES_ID = 0;
    private final static int COL_INDICATES_COUNTER = 1;

    // templates...xls
    private final static int AMOUNT_ROW_IN_HEADER = 1;

    private final static int COL_INDICATES_NAME_GRAPH = 0;
    private final static int COL_INDICATES_WORK_TIME = 1;
    private final static int FIRST_COL_INDICATES_HOUR = 2;

    private final static int COL_INDICATES_NAME_GRAPH_IN_TEMPLATE = 1;
    private final static int COL_INDICATES_WORK_TIME_IN_TEMPLATE = 2;
    private final static int FIRST_COL_INDICATES_HOUR_IN_TEMPLATE = 3;
    private final static int OFFSET_FOR_HOLIDAYS = 2;
    private final static int OFFSET_FOR_SHORT_DAYS = 3;
    private final static int SIZE_STEP = 5;

    public static void writeWorkHoursInFile(final List<DayGraph> graphs, int month, int year) throws Exception {
        FileInputStream fis = new FileInputStream("./_files/templates/templateWorkingTime.xls");
        Workbook wb = new HSSFWorkbook(fis);

        CellStyle styleForDaytime = wb.createCellStyle();
        styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

        CellStyle styleForNightTime = wb.createCellStyle();
        styleForNightTime.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleForNightTime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        for(DayGraph graph : graphs){
            Row row = wb.getSheetAt(0).createRow(graph.getId() + AMOUNT_ROW_IN_HEADER);
            Cell cell = row.createCell(COL_INDICATES_NAME_GRAPH);
            cell.setCellValue(graph.getName());
            cell = row.createCell(COL_INDICATES_WORK_TIME);
            cell.setCellValue(graph.getNormTime());

            for(int indexDay = 0; indexDay < graph.getAmountDay(); ++indexDay){
                if(graph.getWorkTime(indexDay) != 0){
                    cell = row.createCell(FIRST_COL_INDICATES_HOUR + indexDay);
                    cell.setCellValue(graph.getWorkTime(indexDay));
                    if(graph.isNightTime(indexDay)) cell.setCellStyle(styleForNightTime);
                    else cell.setCellStyle(styleForDaytime);
                }
            }
        }
        FileOutputStream fos = new FileOutputStream("./workHours_" + month + "_" + year + ".xls");
        wb.write(fos);

        fos.close();
        wb.close();
        fis.close();
    }

    public static void writeGraphsIntoTemplate(final List<DayGraph> graphs, String nameRegion, final List<String> regions,
                                                int month, int year) throws Exception {

        FileInputStream fis = new FileInputStream("./_files/templates/templateForSapHR.xls");
        Workbook wb = new HSSFWorkbook(fis);

        CellStyle styleForDaytime = wb.createCellStyle();
        styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

        CellStyle styleForNightTime = wb.createCellStyle();
        styleForNightTime.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleForNightTime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        int rowIndexFile = 0;
        for(DayGraph graph : graphs) {
            if(!regions.contains(graph.getName())) continue;
            Row row = wb.getSheetAt(0).createRow(rowIndexFile + AMOUNT_ROW_IN_HEADER);
            Cell cell = row.createCell(COL_INDICATES_NAME_GRAPH_IN_TEMPLATE);
            cell.setCellValue(graph.getName());
            cell = row.createCell(COL_INDICATES_WORK_TIME_IN_TEMPLATE);
            cell.setCellValue(graph.getNormTime());

            for (int indexDay = 0; indexDay < graph.getAmountDay(); ++indexDay) {
                // write hour sign
                cell = row.createCell(FIRST_COL_INDICATES_HOUR_IN_TEMPLATE + indexDay * SIZE_STEP);
                String hourName = graph.getWorkTimeSign(indexDay);
                cell.setCellValue(hourName);

                // set color
                if (!hourName.equals("FREE")) {
                    if (graph.isNightTime(indexDay)) cell.setCellStyle(styleForNightTime);
                    else cell.setCellStyle(styleForDaytime);
                }

                // write short days sign
                if(graph.getShortDaysSign(indexDay) != ' ') {
                    cell = row.createCell(FIRST_COL_INDICATES_HOUR_IN_TEMPLATE + indexDay * SIZE_STEP + OFFSET_FOR_SHORT_DAYS);
                    cell.setCellValue(Character.toString(graph.getShortDaysSign(indexDay)));
                }

                // write holidays sign
                if(graph.getHolidaysSign(indexDay) != ' ') {
                    cell = row.createCell(FIRST_COL_INDICATES_HOUR_IN_TEMPLATE + indexDay * SIZE_STEP + OFFSET_FOR_HOLIDAYS);
                    cell.setCellValue(Integer.parseInt(Character.toString(graph.getHolidaysSign(indexDay))));
                }
            }
            ++rowIndexFile;
        }
        FileOutputStream fos = new FileOutputStream("./" + nameRegion + "_" + month + "_" + year + ".xls");
        wb.write(fos);

        fos.close();
        wb.close();
        fis.close();
    }

    public static void writeNextCounter(final List<DayGraph> graphs, int daysInMonth, int month, int year) throws Exception {
        String filename = "counter_" + month + "_" + year + ".xls";
        FileInputStream fis = new FileInputStream("./_files/counters/" + filename);
        Workbook wb = new HSSFWorkbook(fis);

        for(DayGraph graph : graphs){
            int nextCounter = (daysInMonth + graph.getCounter()) % graph.getLengthRule();
            Row row = wb.getSheetAt(0).getRow(graph.getId());
            Cell cell = row.createCell(COL_INDICATES_ID);
            cell.setCellValue(graph.getId());
            cell = row.createCell(COL_INDICATES_COUNTER);
            cell.setCellValue(nextCounter);
        }

        String nameNextFile = "counter_" + (month + 1) + "_" + year + ".xls";
        if(month + 1 > MAX_INDEX_MONTH) nameNextFile = "counter_" + "1" + "_" + (year + 1) + ".xls";
        FileOutputStream fos = new FileOutputStream("./_files/counters/" + nameNextFile);
        wb.write(fos);

        fos.close();
        wb.close();
        fis.close();
    }

    public static void deleteOldCounter(int month, int year){
        String filenameOldCounter = "counter_" + month + "_" + (year - 1) + ".xls";
        File oldFile = new File("./_files/counters/" + filenameOldCounter);
        if(oldFile.delete()) System.out.println("Удален счетчик за " + month + "." + (year - 1));
    }
}