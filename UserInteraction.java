package ru.polynkina.irina.graphs;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.util.Map;

public class UserInteraction {

    final static int MAX_INDEX_YEAR = 2116;
    final static int MIN_INDEX_YEAR = 2016;
    final static int MAX_INDEX_MONTH = 12;
    final static int MIN_INDEX_MONTH = 1;
    final static int MAX_NORM_TIME = 200;
    final static int MIN_NORM_TIME = 100;
    final static int BITS_IN_MONTH = 4;
    final static int MASK_FOR_MONTH = 0xF;

    final static int INDEX_OF_SHEET = 0;
    final static int ROW_INDEX_OF_YEAR = 0;
    final static int ROW_INDEX_OF_MONTH = 1;
    final static int ROW_INDEX_OF_NORM_TIME = 2;
    final static int ROW_INDEX_OF_SHORT_DAY = 3;
    final static int ROW_INDEX_OF_DAY_OFF = 5;
    final static int COL_INDEX_OF_DATE = 1;
    final static int DELTA_INDEX_AND_VALUE = 3;

    public static int readYearAndMonth(){
        int yearAndMonth = 0;
        try{

            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);
            int year = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(ROW_INDEX_OF_YEAR).getCell(COL_INDEX_OF_DATE).getNumericCellValue();
            int month = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(ROW_INDEX_OF_MONTH).getCell(COL_INDEX_OF_DATE).getNumericCellValue();

            boolean isIncorrectInput = false;
            if(year < MIN_INDEX_YEAR || year > MAX_INDEX_YEAR) isIncorrectInput = true;
            if(month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH) isIncorrectInput = true;
            if(isIncorrectInput) throw new Exception("Некорректно указан месяц или год!");
            yearAndMonth = year << BITS_IN_MONTH | month;

            wb.close();
            fis.close();

        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }

        return yearAndMonth;
    }



    public static double readNormTime(){
        double normTime = 0;
        try{

            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);
            normTime = wb.getSheetAt(INDEX_OF_SHEET).getRow(ROW_INDEX_OF_NORM_TIME).getCell(COL_INDEX_OF_DATE).getNumericCellValue();
            if(normTime < MIN_NORM_TIME || normTime > MAX_NORM_TIME)
                throw new Exception("Некорректно указана норма времени!");

            wb.close();
            fis.close();

        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }

        return normTime;
    }



    public static  void readShortDayAndHolidays(Map<Integer, Integer> shortDayAndHolidays, int amountDay){
        try{

            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);

            for(int indexRow = ROW_INDEX_OF_SHORT_DAY; indexRow <= ROW_INDEX_OF_DAY_OFF; ++indexRow){
                for(int indexCol = COL_INDEX_OF_DATE; indexCol <= amountDay; ++indexCol){
                    try{

                        int indexDay = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(indexCol).getNumericCellValue();
                        if(indexDay == 0) throw new NullPointerException();

                        boolean isNewDate = shortDayAndHolidays.get(indexDay) == null;
                        if(!isNewDate) throw new Exception("Значение " + indexDay + " не может быть указано дважды");

                        if(indexDay <= 0 || indexDay > amountDay)
                            throw new Exception(indexDay + "-й день не существует");

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