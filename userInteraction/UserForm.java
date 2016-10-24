package ru.polynkina.irina.userInteraction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import ru.polynkina.irina.main.Main;

import java.io.FileInputStream;
import java.util.Map;

public class UserForm {

    // constants in the file main.xls
    final static int DELTA_INDEX_AND_VALUE = 3;
    final static int INDEX_OF_SHEET = 0;
    final static int ROW_INDEX_OF_YEAR = 0;
    final static int ROW_INDEX_OF_MONTH = 1;
    final static int ROW_INDEX_OF_NORM_TIME = 2;
    final static int ROW_INDEX_OF_SHORT_DAY = 3;
    final static int ROW_INDEX_OF_DAY_OFF = 5;
    final static int COL_INDEX_OF_DATE = 1;


    public static int readYear(){
        final int MAX_INDEX_YEAR = 2116;
        final int MIN_INDEX_YEAR = 2016;
        int year = 0;
        try{
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
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
        final int MAX_INDEX_MONTH = 12;
        final int MIN_INDEX_MONTH = 1;
        int month = 0;
        try {
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
            Workbook wb = new HSSFWorkbook(fis);
            month = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(ROW_INDEX_OF_MONTH).getCell(COL_INDEX_OF_DATE).getNumericCellValue();
            wb.close();
            fis.close();

            if (month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH) {
                throw new Exception("Некорректно указан месяц!");
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(0);
        }
        return month;
    }



    public static double readNormTime(){
        final int MAX_NORM_TIME = 200;
        final int MIN_NORM_TIME = 100;
        double normTime = 0;
        try{
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
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



    public static void readShortDayAndHolidays(Map<Integer, Integer> shortDayAndHolidays){
        try{
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
            Workbook wb = new HSSFWorkbook(fis);

            for(int indexRow = ROW_INDEX_OF_SHORT_DAY; indexRow <= ROW_INDEX_OF_DAY_OFF; ++indexRow){
                for(int indexCol = COL_INDEX_OF_DATE; indexCol <= Main.DAY_OF_MONTH; ++indexCol){
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
                        if(indexDay <= 0 || indexDay > Main.DAY_OF_MONTH) isIncorrectInput = true;
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