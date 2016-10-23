package ru.polynkina.irina.graphs;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.util.Map;

public class UserInteraction {

    final static int MAX_INDEX_YEAR = 2116;
    final static int MIN_INDEX_YEAR = 2016;
    final static int MAX_NORM_TIME = 200;
    final static int MIN_NORM_TIME = 100;

    // constants in the file graphs.xls
    final static int DELTA_INDEX_AND_VALUE = 3;
    final static int INDEX_OF_SHEET = 0;
    final static int ROW_INDEX_OF_YEAR = 0;
    final static int ROW_INDEX_OF_MONTH = 1;
    final static int ROW_INDEX_OF_NORM_TIME = 2;
    final static int ROW_INDEX_OF_SHORT_DAY = 3;
    final static int ROW_INDEX_OF_DAY_OFF = 5;
    final static int COL_INDEX_OF_DATE = 1;


    public static int readYear(){
        int year = 0;
        try{
            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);
            year = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(ROW_INDEX_OF_YEAR).getCell(COL_INDEX_OF_DATE).getNumericCellValue();
            wb.close();
            fis.close();

            if(year < MIN_INDEX_YEAR || year > MAX_INDEX_YEAR) {
                throw new Exception("Некорректно указан месяц или год!");
            }
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        return year;
    }


    public static int readMonth() {
        int month = 0;
        try {
            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);
            month = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(ROW_INDEX_OF_MONTH).getCell(COL_INDEX_OF_DATE).getNumericCellValue();
            wb.close();
            fis.close();

            if (month < Calendar.MIN_INDEX_MONTH || month > Calendar.MAX_INDEX_MONTH) {
                throw new Exception("Некорректно указан месяц!");
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(0);
        }
        return month;
    }



    public static double readNormTime(){
        double normTime = 0;
        try{
            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);
            normTime = wb.getSheetAt(INDEX_OF_SHEET).getRow(ROW_INDEX_OF_NORM_TIME).getCell(COL_INDEX_OF_DATE).getNumericCellValue();
            wb.close();
            fis.close();

            boolean isIncorrectInput = false;
            if(normTime < MIN_NORM_TIME || normTime > MAX_NORM_TIME) isIncorrectInput = true;
            if(isIncorrectInput) throw new Exception("Некорректно указана норма времени!");
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        return normTime;
    }



    public static  void readShortDayAndHolidays(Map<Integer, Integer> shortDayAndHolidays){
        try{
            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);

            for(int indexRow = ROW_INDEX_OF_SHORT_DAY; indexRow <= ROW_INDEX_OF_DAY_OFF; ++indexRow){
                for(int indexCol = COL_INDEX_OF_DATE; indexCol <= CreatingGraphs.AMOUNT_OF_DAYS; ++indexCol){
                    try{
                        int indexDay = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(indexCol).getNumericCellValue();
                        if(indexDay == 0) throw new NullPointerException();

                        boolean isNewDate = shortDayAndHolidays.get(indexDay) == null;
                        if(!isNewDate){
                            wb.close();
                            fis.close();
                            throw new Exception("Значение " + indexDay + " не может быть указано дважды");
                        }

                        boolean isIncorrectInput = false;
                        if(indexDay <= 0 || indexDay > CreatingGraphs.AMOUNT_OF_DAYS) isIncorrectInput = true;
                        if(isIncorrectInput){
                            wb.close();
                            fis.close();
                            throw new Exception(indexDay + "-й день не существует");
                        }

                        shortDayAndHolidays.put(indexDay, indexRow - DELTA_INDEX_AND_VALUE);
                    }catch(NullPointerException nullExc){
                        break;
                    }
                }
            }

            wb.close();
            fis.close();
        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }
}