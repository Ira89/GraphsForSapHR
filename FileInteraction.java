package ru.polynkina.irina.graphs;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class FileInteraction {

    final static int INDEX_OF_SHEET = 0;

    // constants in the file graphs.xls
    final static int COL_INDEX_OF_ID = 0;
    final static int COL_INDEX_OF_NAME = 1;
    final static int COL_INDEX_OF_RULE = 2;
    final static int COL_INDEX_OF_TYPE = 3;
    final static int COL_INDEX_OF_DAYTIME = 4;
    final static int COL_INDEX_OF_DAYTIME_SIGN = 5;
    final static String TYPE_DESIGNATION_OF_DAY_GRAPHS = "DAY";

    // constants in the file counter_...xls
    final static int COL_INDEX_COUNTER_ID = 0;
    final static int COL_INDEX_COUNTER_VALUE = 1;

    // constants in the file templateWorkingTime.xls and in the file templateForSapHR.xls
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
                                                        private methods
     ******************************************************************************************************************************************/


    private static String findHourName(Map<Double, String> hours, double desiredValue){
        String hourName = hours.get(desiredValue);
        if(hourName == null){
            System.out.println("Не могу найти график на: " + desiredValue + " час");
            hourName = "FREE";
        }
        return hourName;
    }


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    public static void fabricateGraphs(List<Graph> graphs){
        try{

            FileInputStream fis = new FileInputStream("./lib/graphs.xls");
            Workbook wb = new HSSFWorkbook(fis);

            int indexRow = 0;
            while(true){
                try{

                    int id = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_ID).getNumericCellValue();
                    String name = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_NAME).getStringCellValue();
                    String rule = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_RULE).getStringCellValue();
                    String type = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_TYPE).getStringCellValue();
                    double daytime = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_DAYTIME).getNumericCellValue();
                    String daytimeSign = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_DAYTIME_SIGN).getStringCellValue();
                    ++indexRow;

                    if(type.equals(TYPE_DESIGNATION_OF_DAY_GRAPHS)){
                        graphs.add(new Graph(id, name, rule, daytime, daytimeSign));
                    }else{
                        //TODO
                        graphs.add(new Graph(id, name, rule, daytime, daytimeSign));
                    }

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



    public static void readCountersForGraphs(List<Graph> graphs, int indexMonth, int indexYear){
        try{

            String filename = "counter_" + indexMonth + "_" + indexYear + ".xls";
            FileInputStream fis = new FileInputStream("./count/" + filename);
            Workbook wb = new HSSFWorkbook(fis);

            for(Graph obj : graphs){
                int idGraph = obj.getId();
                int idCounter = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(idGraph).getCell(COL_INDEX_COUNTER_ID).getNumericCellValue();
                int valueCounter = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(idGraph).getCell(COL_INDEX_COUNTER_VALUE).getNumericCellValue();

                if(idGraph != idCounter) throw new Exception("Вероятно, файл " + filename + " поврежден");
                obj.setCounter(valueCounter);
            }

            wb.close();
            fis.close();

        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void writeWorkTimeInFile(List<Graph> graphs, int amountDay){
        try{

            FileInputStream fis = new FileInputStream("./lib/templateWorkingTime.xls");
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
                cell.setCellValue(obj.getSumWorkTime(amountDay));

                int lengthRule = obj.getLengthRule();
                int currentCounter = obj.getCounter();
                for(int indexDay = 0; indexDay < amountDay; ++indexDay){
                    if(obj.getWorkTime(indexDay) != 0){
                        cell = row.createCell(indexDay + DELTA_COL_IN_TEMPLATE);
                        cell.setCellValue(obj.getWorkTime(indexDay));
                        if(obj.getRuleOfDay(currentCounter) == Graph.TYPE_DESIGNATION_NIGHT){
                            cell.setCellStyle(styleForNighttime);
                        }
                        else if((obj.getRuleOfDay(currentCounter) == Graph.TYPE_DESIGNATION_DAY) ||
                                (obj.getRuleOfDay(currentCounter) == Graph.TYPE_DESIGNATION_UNIVERSAL_DAY)){
                            cell.setCellStyle(styleForDaytime);
                        }
                    }
                    if(++currentCounter == lengthRule) currentCounter = 0;
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

            FileInputStream fis = new FileInputStream("./lib/dayHours.xls");
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

            FileInputStream fis = new FileInputStream("./lib/nightHours.xls");
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



    public static void writeGraphsIntoTemplate(List<Graph> graphs, Map<Double, String> dayHours, Map<Double, String> nightHours,
                                               Map<Integer, Integer> shortDayAndHoliday, int amountDay){
        try{

            FileInputStream fis = new FileInputStream("./lib/templateForSapHR.xls");
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
                cell.setCellValue(obj.getSumWorkTime(amountDay));

                int lengthRule = obj.getLengthRule();
                int currentCounter = obj.getCounter();
                for(int indexDay = 0; indexDay < amountDay; ++indexDay){
                    double hour = obj.getWorkTime(indexDay);
                    Integer codeDay = shortDayAndHoliday.get(indexDay + 1);
                    if(codeDay != null && codeDay == Graph.TYPE_DESIGNATION_SHORT_DAY){
                        if(obj.getWorkTime(indexDay) != 0) ++hour;
                    }

                    String hourName;
                    if(obj.getRuleOfDay(currentCounter) == Graph.TYPE_DESIGNATION_DAY){
                        if(hour == obj.getDaytime()) hourName = obj.getDaytimeSign();
                        else hourName = findHourName(dayHours, hour);
                    }
                    else if(obj.getRuleOfDay(currentCounter) == Graph.TYPE_DESIGNATION_NIGHT){
                        //TODO
                        hourName = findHourName(nightHours, hour);
                    }
                    else{
                        //TODO
                        hourName = findHourName(dayHours, hour);
                    }

                    cell = row.createCell(DELTA_COL_IN_TEMPLATE + indexDay * SIZE_STEP);
                    cell.setCellValue(hourName);

                    if(obj.getRuleOfDay(currentCounter) == Graph.TYPE_DESIGNATION_WEEKEND && obj.getWorkTime(indexDay) == 0);
                    else if(obj.getRuleOfDay(currentCounter) == Graph.TYPE_DESIGNATION_NIGHT) cell.setCellStyle(styleForNighttime);
                    else cell.setCellStyle(styleForDaytime);

                    // set sign short day and holiday
                    if(codeDay != null){
                        switch(codeDay){
                            case Graph.TYPE_DESIGNATION_SHORT_DAY:{
                                if(obj.getRuleOfDay(currentCounter) != Graph.TYPE_DESIGNATION_WEEKEND){
                                    cell = row.createCell(DELTA_COL_IN_TEMPLATE + (indexDay + 1) * SIZE_STEP - CELL_OFFSET_FOR_TEXT);
                                    cell.setCellValue(SIGN_SHORT_DAY);
                                }
                            } break;
                            case Graph.TYPE_DESIGNATION_HOLIDAY:{
                                cell = row.createCell(DELTA_COL_IN_TEMPLATE + (indexDay + 1) * SIZE_STEP - CELL_OFFSET_FOR_NUMBER);
                                cell.setCellValue(SIGN_HOLIDAY);
                                //TODO
                            } break;
                            case Graph.TYPE_DESIGNATION_DAY_OFF:{
                                //TODO
                            }
                        }
                    }
                    if(++currentCounter == lengthRule) currentCounter = 0;
                }
            }

            FileOutputStream fos = new FileOutputStream("./fileForSapHR.xls");
            wb.write(fos);

            fos.close();
            wb.close();
            fis.close();

        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }
}