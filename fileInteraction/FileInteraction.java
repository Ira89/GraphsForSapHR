package ru.polynkina.irina.fileInteraction;

import ru.polynkina.irina.graphs.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.List;
import java.io.File;

public class FileInteraction {

    final static int INDEX_OF_SHEET = 0;

    // constants in the file counter_...xls
    final static int COL_INDEX_COUNTER_ID = 0;
    final static int COL_INDEX_COUNTER_VALUE = 1;
    final static int DATA_HOLD_TIME = 2;

    // constants in the file template...xls
    final static int DELTA_ROW_IN_TEMPLATE = 1;
    final static int DELTA_COL_IN_TEMPLATE = 2;
    final static int DELTA_COL_IN_TEMPLATE_SAP = 3;
    final static int COL_INDEX_NAME_GRAPH_IN_TEMPLATE = 0;
    final static int COL_INDEX_NAME_GRAPH_IN_TEMPLATE_SAP = 1;
    final static int COL_INDEX_WORK_TIME_IN_TEMPLATE = 1;
    final static int COL_INDEX_WORK_TIME_IN_TEMPLATE_SAP = 2;
    final static int SIZE_STEP = 5;

    final static int CELL_OFFSET_FOR_TEXT = 2;
    final static int CELL_OFFSET_FOR_NUMBER = 3;

    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/

    public static void writeWorkTimeInFile(final List<DayGraph> graphs, final int AMOUNT_OF_DAYS){
        try{
            FileInputStream fis = new FileInputStream("./_files/templates/templateWorkingTime.xls");
            Workbook wb = new HSSFWorkbook(fis);

            CellStyle styleForDaytime = wb.createCellStyle();
            styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

            CellStyle styleForNighttime = wb.createCellStyle();
            styleForNighttime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForNighttime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

            for(DayGraph obj : graphs){
                Row row = wb.getSheetAt(INDEX_OF_SHEET).createRow(obj.getId() + DELTA_ROW_IN_TEMPLATE);
                Cell cell = row.createCell(COL_INDEX_NAME_GRAPH_IN_TEMPLATE);
                cell.setCellValue(obj.getName());
                cell = row.createCell(COL_INDEX_WORK_TIME_IN_TEMPLATE);
                cell.setCellValue(obj.getNormTime());

                for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
                    if(obj.getWorkTime(indexDay) != 0){
                        cell = row.createCell(indexDay + DELTA_COL_IN_TEMPLATE);
                        cell.setCellValue(obj.getWorkTime(indexDay));
                        if(obj.getRuleOfDay(indexDay) == 'n') cell.setCellStyle(styleForNighttime);
                        else cell.setCellStyle(styleForDaytime);
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream("./workTime.xls");
            wb.write(fos);

            fos.close();
            wb.close();
            fis.close();
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }


    public static void writeGraphsIntoTemplate(final List<DayGraph> graphs, final List<String> regions, String nameRegion,
                                                int month, int year){
        try{
            FileInputStream fis = new FileInputStream("./_files/templates/templateForSapHR.xls");
            Workbook wb = new HSSFWorkbook(fis);

            CellStyle styleForDaytime = wb.createCellStyle();
            styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

            CellStyle styleForNighttime = wb.createCellStyle();
            styleForNighttime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForNighttime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

            int indexRow = 0;
            for(DayGraph obj : graphs) {
                if(!regions.contains(obj.getName())) continue;
                Row row = wb.getSheetAt(INDEX_OF_SHEET).createRow(indexRow + DELTA_ROW_IN_TEMPLATE);
                Cell cell = row.createCell(COL_INDEX_NAME_GRAPH_IN_TEMPLATE_SAP);
                cell.setCellValue(obj.getName());
                cell = row.createCell(COL_INDEX_WORK_TIME_IN_TEMPLATE_SAP);
                cell.setCellValue(obj.getNormTime());

                for (int indexDay = 0; indexDay < obj.getAmountDay(); ++indexDay) {
                    // write hour sign
                    cell = row.createCell(DELTA_COL_IN_TEMPLATE_SAP + indexDay * SIZE_STEP);
                    String hourName = obj.getWorkTimeSign(indexDay);
                    cell.setCellValue(hourName);

                    // set color
                    if (!hourName.equals("FREE")) {
                        if (obj.getRuleOfDay(indexDay) == 'n') cell.setCellStyle(styleForNighttime);
                        else cell.setCellStyle(styleForDaytime);
                    }

                    // write short days sign
                    if(!obj.getShortDaysSign(indexDay).equals("")) {
                        cell = row.createCell(DELTA_COL_IN_TEMPLATE_SAP + indexDay * SIZE_STEP + CELL_OFFSET_FOR_TEXT + 1);
                        cell.setCellValue(obj.getShortDaysSign(indexDay));
                    }

                    // write holidays sign
                    if(!obj.getHolidaysSign(indexDay).equals("")) {
                        cell = row.createCell(DELTA_COL_IN_TEMPLATE_SAP + indexDay * SIZE_STEP + CELL_OFFSET_FOR_TEXT);
                        cell.setCellValue(Integer.parseInt(obj.getHolidaysSign(indexDay)));
                    }
                }
                ++indexRow;
            }
            FileOutputStream fos = new FileOutputStream("./" + nameRegion + "_" + month + "." + year + ".xls");
            wb.write(fos);

            fos.close();
            wb.close();
            fis.close();
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void writeNextCounter(final List<DayGraph> graphs, int dayOfMonth, int month, int year){
        final int MAX_INDEX_MONTH = 12;
        String filename = "counter_" + month + "_" + year + ".xls";
        try{
            FileInputStream fis = new FileInputStream("./_files/counters/" + filename);
            Workbook wb = new HSSFWorkbook(fis);

            for(DayGraph obj : graphs){
                Row row = wb.getSheetAt(0).getRow(obj.getId());
                Cell cell = row.createCell(COL_INDEX_COUNTER_ID);
                cell.setCellValue(obj.getId());

                int nextCounter = dayOfMonth % obj.getLengthRule();
                nextCounter += obj.getCounter();
                nextCounter %= obj.getLengthRule();
                cell = row.createCell(COL_INDEX_COUNTER_VALUE);
                cell.setCellValue(nextCounter);
            }

            int nextMonth = month + 1 > MAX_INDEX_MONTH ? 1 : month + 1;
            int nextYear = month + 1 > MAX_INDEX_MONTH ? year + 1 : year;
            String nextFilename = "counter_" + nextMonth + "_" + nextYear + ".xls";
            FileOutputStream fos = new FileOutputStream("./_files/counters/" + nextFilename);
            wb.write(fos);

            fos.close();
            wb.close();
            fis.close();
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void deleteOldCounter(int month, int year){
        try{
            String filenameOldCounter = "counter_" + month + "_" + (year - DATA_HOLD_TIME) + ".xls";
            File oldFile = new File("./_files/counters/" + filenameOldCounter);
            if(oldFile.delete()) System.out.println("Удален счетчик за " + month + "." + (year - DATA_HOLD_TIME));
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }

}