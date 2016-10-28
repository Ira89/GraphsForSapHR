package ru.polynkina.irina.fileInteraction;

import ru.polynkina.irina.graphs.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.io.File;

public class FileInteraction {

    final static int INDEX_OF_SHEET = 0;

    // constants in the file main.xls
    final static int COL_INDEX_OF_ID = 0;
    final static int COL_INDEX_OF_NAME = 1;
    final static int COL_INDEX_OF_RULE = 2;
    final static int COL_INDEX_OF_TYPE = 3;
    final static int COL_INDEX_OF_DAYTIME = 4;
    final static int COL_INDEX_OF_DAYTIME_SIGN = 5;
    final static int COL_INDEX_OF_NIGHTTIME = 6;
    final static int COL_INDEX_OF_NIGHTTIME_SIGN = 7;

    final static String STRING_DESIGNATION_OF_STANDARD_GRAPHS = "STANDARD";
    final static String STRING_DESIGNATION_OF_DIURNAL_GRAPHS = "DIURNAL";
    final static String STRING_DESIGNATION_OF_SHORT_DAY_GRAPHS = "SHORT";
    final static String STRING_DESIGNATION_OF_UNIQUE_GRAPHS = "UNIQUE";
    final static String STRING_DESIGNATION_OF_FLOAT_GRAPHS = "FLOAT";
    final static String STRING_DESIGNATION_OF_DAY_GRAPHS = "DAY";
    final static String STRING_DESIGNATION_OF_MIX_GRAPHS = "MIX";

    // constants in the file counter_...xls
    final static int COL_INDEX_COUNTER_ID = 0;
    final static int COL_INDEX_COUNTER_VALUE = 1;
    final static int DATA_HOLD_TIME = 2;

    // constants in the file template...xls
    final static int DELTA_ROW_IN_TEMPLATE = 1;
    final static int DELTA_COL_IN_TEMPLATE = 2;
    final static int COL_INDEX_NAME_GRAPH_IN_TEMPLATE = 0;
    final static int COL_INDEX_WORK_TIME_IN_TEMPLATE = 1;
    final static int SIZE_STEP = 5;

    final static String SIGN_SHORT_DAY = "A";
    final static String SIGN_DAY_OFF = "F";
    final static int SIGN_HOLIDAY = 1;

    final static int CELL_OFFSET_FOR_TEXT = 2;
    final static int CELL_OFFSET_FOR_NUMBER = 3;

    // constants in the file dayHours.xls and in the file nightHours.xls
    final static int COL_INDEX_HOUR = 0;
    final static int COL_INDEX_HOUR_NAME = 1;


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    public static void fabricateGraphs(List<Graph> graphs){
        try{
            String filename = "rules.xls";
            FileInputStream fis = new FileInputStream("./rules/" + filename);
            Workbook wb = new HSSFWorkbook(fis);

            int indexRow = 0;
            while(true){
                try{
                    int id = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_ID).getNumericCellValue();
                    if (id != indexRow) throw new Exception("Файл " + filename + " поврежден!");

                    String name = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_NAME).getStringCellValue();
                    String rule = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_RULE).getStringCellValue();
                    String type = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_TYPE).getStringCellValue();
                    double daytime = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_DAYTIME).getNumericCellValue();
                    String daytimeSign = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_DAYTIME_SIGN).getStringCellValue();

                    if(type.equals(STRING_DESIGNATION_OF_DAY_GRAPHS)){
                        graphs.add(new Graph(id, name, rule, daytime, daytimeSign));
                    }
                    else if(type.equals(STRING_DESIGNATION_OF_SHORT_DAY_GRAPHS)){
                        graphs.add(new GraphShort(id, name, rule, daytime, daytimeSign));
                    }
                    else if(type.equals(STRING_DESIGNATION_OF_STANDARD_GRAPHS)){
                        graphs.add(new GraphStandard(id, name, rule, daytime, daytimeSign));
                    }
                    else if(type.equals(STRING_DESIGNATION_OF_UNIQUE_GRAPHS)){
                        double uniqueTime = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_NIGHTTIME).getNumericCellValue();
                        String uTimeSign = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_NIGHTTIME_SIGN).getStringCellValue();
                        graphs.add(new GraphUnique(id, name, rule, daytime, daytimeSign, uniqueTime, uTimeSign));
                    }
                    else if(type.equals(STRING_DESIGNATION_OF_FLOAT_GRAPHS)){
                        graphs.add(new GraphFloat(id, name, rule, daytime, daytimeSign));
                    }
                    else if(type.equals(STRING_DESIGNATION_OF_DIURNAL_GRAPHS)){
                        graphs.add(new GraphDiurnal(id, name, rule, daytime, daytimeSign));
                    }
                    else if(type.equals(STRING_DESIGNATION_OF_MIX_GRAPHS)){
                        double nightTime = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_NIGHTTIME).getNumericCellValue();
                        String nTimeSign = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_NIGHTTIME_SIGN).getStringCellValue();
                        graphs.add(new GraphMix(id, name, rule, daytime, daytimeSign, nightTime, nTimeSign));
                    }
                    else{
                        throw new Exception("Тип графика: " + type + " неизвестен!");
                    }

                    ++indexRow;
                }catch(NullPointerException nullExc){
                    break;
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }

            wb.close();
            fis.close();
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void readCountersForGraphs(List<Graph> graphs, int month, int year){
        try {
            String filename = "counter_" + month + "_" + year + ".xls";
            FileInputStream fis = new FileInputStream("./counters/" + filename);
            Workbook wb = new HSSFWorkbook(fis);

            for (Graph obj : graphs) {
                int idGraph = obj.getId();
                int idCounter = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(idGraph).getCell(COL_INDEX_COUNTER_ID).getNumericCellValue();
                if (idGraph != idCounter){
                    wb.close();
                    fis.close();
                    throw new Exception("Файл " + filename + " поврежден");
                }

                int valueCounter = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(idGraph).getCell(COL_INDEX_COUNTER_VALUE).getNumericCellValue();
                obj.setCounter(valueCounter);
            }

            wb.close();
            fis.close();
        }catch(FileNotFoundException excFile){
            System.out.println("Не сгенерирован график за предыдущий месяц!");
            System.exit(0);
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void writeWorkTimeInFile(final List<Graph> graphs, final int AMOUNT_OF_DAYS){
        try{
            FileInputStream fis = new FileInputStream("./templates/templateWorkingTime.xls");
            Workbook wb = new HSSFWorkbook(fis);

            CellStyle styleForDaytime = wb.createCellStyle();
            styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

            CellStyle styleForNighttime = wb.createCellStyle();
            styleForNighttime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForNighttime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

            for(Graph obj : graphs){
                Row row = wb.getSheetAt(INDEX_OF_SHEET).createRow(obj.getId() + DELTA_ROW_IN_TEMPLATE);
                Cell cell = row.createCell(COL_INDEX_NAME_GRAPH_IN_TEMPLATE);
                cell.setCellValue(obj.getName());
                cell = row.createCell(COL_INDEX_WORK_TIME_IN_TEMPLATE);
                cell.setCellValue(obj.getNormTime());

                for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
                    if(obj.getWorkTime(indexDay) != 0){
                        cell = row.createCell(indexDay + DELTA_COL_IN_TEMPLATE);
                        cell.setCellValue(obj.getWorkTime(indexDay));

                        boolean isNightGraph = obj.getRuleOfDay(indexDay) == Graph.CHAR_DESIGNATION_NIGHT;
                        if(isNightGraph) cell.setCellStyle(styleForNighttime);
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



    public static void readDayHours(Map<Double, String> dayHours){
        try{
            FileInputStream fis = new FileInputStream("./library/dayHours.xls");
            Workbook wb = new HSSFWorkbook(fis);

            int indexRow = 0;
            while(true){
                try{
                    double hour = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_HOUR).getNumericCellValue();
                    String hourName = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_HOUR_NAME).getStringCellValue();
                    dayHours.put(hour, hourName);
                    ++indexRow;
                }catch(NullPointerException nullExc){
                    break;
                }
            }

            wb.close();
            fis.close();
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void readNightHours(Map<Double, String> nightHours){
        try{
            FileInputStream fis = new FileInputStream("./library/nightHours.xls");
            Workbook wb = new HSSFWorkbook(fis);

            int indexRow = 0;
            while(true){
                try{
                    double hour = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_HOUR).getNumericCellValue();
                    String hourName = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_HOUR_NAME).getStringCellValue();
                    nightHours.put(hour, hourName);
                    ++indexRow;
                }catch(NullPointerException nullExc){
                    break;
                }
            }

            wb.close();
            fis.close();
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }


    /*******************************************************************************************************************************************
                                                        All data written to the file at a time.
                                                        Otherwise, the data will be overwritten.
     /*******************************************************************************************************************************************/


    public static void writeGraphsIntoTemplate(final List<Graph> graphs, final Map<Integer, Integer> shortDayAndHoliday){
        final int CODE_SHORT_DAY = 0;
        final int CODE_HOLIDAY = 1;
        final int CODE_DAY_OFF = 2;
        try{
            FileInputStream fis = new FileInputStream("./templates/templateForSapHR.xls");
            Workbook wb = new HSSFWorkbook(fis);

            CellStyle styleForDaytime = wb.createCellStyle();
            styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

            CellStyle styleForNighttime = wb.createCellStyle();
            styleForNighttime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForNighttime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

            for(Graph obj : graphs) {
                Row row = wb.getSheetAt(INDEX_OF_SHEET).createRow(obj.getId() + DELTA_ROW_IN_TEMPLATE);
                Cell cell = row.createCell(COL_INDEX_NAME_GRAPH_IN_TEMPLATE);
                cell.setCellValue(obj.getName());
                cell = row.createCell(COL_INDEX_WORK_TIME_IN_TEMPLATE);
                cell.setCellValue(obj.getNormTime());

                for (int indexDay = 0; indexDay < obj.getAmountDay(); ++indexDay) {
                    cell = row.createCell(DELTA_COL_IN_TEMPLATE + indexDay * SIZE_STEP);
                    String hourName = obj.getWorkTimeSign(indexDay);
                    cell.setCellValue(hourName);
                    if (!hourName.equals("FREE")) {
                        if (obj.getRuleOfDay(indexDay) == 'n') cell.setCellStyle(styleForNighttime);
                        else cell.setCellStyle(styleForDaytime);
                    }
                }

                for (Map.Entry<Integer, Integer> days : shortDayAndHoliday.entrySet()) {
                    switch (days.getValue()) {
                        case CODE_SHORT_DAY: {
                            if (obj.getRuleOfDay(days.getKey() - 1) != 'f') {
                                cell = row.createCell(DELTA_COL_IN_TEMPLATE + days.getKey() * SIZE_STEP - CELL_OFFSET_FOR_TEXT);
                                cell.setCellValue(SIGN_SHORT_DAY);
                            }
                        }
                        break;
                        case CODE_HOLIDAY: {
                            cell = row.createCell(DELTA_COL_IN_TEMPLATE + days.getKey() * SIZE_STEP - CELL_OFFSET_FOR_NUMBER);
                            cell.setCellValue(SIGN_HOLIDAY);
                        } // without break!
                        case CODE_DAY_OFF: {
                            boolean isStandardOrUniqueGraph = (obj instanceof GraphStandard) || (obj instanceof GraphUnique);
                            if (isStandardOrUniqueGraph && obj.getRuleOfDay(days.getKey() - 1) != 'f') {
                                cell = row.createCell(DELTA_COL_IN_TEMPLATE + days.getKey() * SIZE_STEP - CELL_OFFSET_FOR_TEXT);
                                cell.setCellValue(SIGN_DAY_OFF);
                            }
                        }
                    }
                }
            }
            FileOutputStream fos = new FileOutputStream("./workTimeForSapHR.xls");
            wb.write(fos);

            fos.close();
            wb.close();
            fis.close();
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void writeNextCounter(final List<Graph> graphs, int dayOfMonth, int month, int year){
        final int MAX_INDEX_MONTH = 12;
        String filename = "counter_" + month + "_" + year + ".xls";
        try{
            FileInputStream fis = new FileInputStream("./counters/" + filename);
            Workbook wb = new HSSFWorkbook(fis);

            for(Graph obj : graphs){
                Row row = wb.getSheetAt(INDEX_OF_SHEET).getRow(obj.getId());
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
            FileOutputStream fos = new FileOutputStream("./counters/" + nextFilename);
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
            File oldFile = new File("./counters/" + filenameOldCounter);
            if(oldFile.delete()){
                System.out.println("Программа удалила файл за " + month + "." + (year - DATA_HOLD_TIME));
            }
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }
}