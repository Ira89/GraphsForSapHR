package ru.polynkina.irina.userInteraction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.util.Map;

public class UserForm {

    private final static int ROW_INDICATES_YEAR = 0;
    private final static int COL_INDICATES_YEAR = 1;

    private final static int MIN_INDEX_MONTH = 1;
    private final static int MAX_INDEX_MONTH = 12;
    private final static int ROW_INDICATES_MONTH = 1;
    private final static int COL_INDICATES_MONTH = 1;

    private final static int MIN_NORM_TIME = 100;
    private final static int MAX_NORM_TIME = 200;
    private final static int ROW_INDICATES_NORM_TIME = 2;
    private final static int COL_INDICATES_NORM_TIME = 1;

    private final static int ROW_INDICATES_SHORT_DAY = 3;
    private final static int ROW_INDICATES_DAY_OFF = 5;
    private final static int COL_INDICATES_DATE = 1;
    private final static int AMOUNT_OF_HEADER = 3;


    public static int readYear() {
        int year = 0;
        try {
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
            Workbook wb = new HSSFWorkbook(fis);
            year = (int) wb.getSheetAt(0).getRow(ROW_INDICATES_YEAR).getCell(COL_INDICATES_YEAR).getNumericCellValue();
            wb.close();
            fis.close();
        } catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        return year;
    }


    public static int readMonth() {
        int month = 0;
        try {
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
            Workbook wb = new HSSFWorkbook(fis);
            month = (int) wb.getSheetAt(0).getRow(ROW_INDICATES_MONTH).getCell(COL_INDICATES_MONTH).getNumericCellValue();
            wb.close();
            fis.close();

            if (month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH) {
                throw new Exception("Месяц должен быть в диапазоне от " + MIN_INDEX_MONTH + " до " + MAX_INDEX_MONTH);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(0);
        }
        return month;
    }


    public static double readNormTime() {
        double normTime = 0;
        try{
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
            Workbook wb = new HSSFWorkbook(fis);
            normTime = wb.getSheetAt(0).getRow(ROW_INDICATES_NORM_TIME).getCell(COL_INDICATES_NORM_TIME).getNumericCellValue();
            wb.close();
            fis.close();

            if(normTime < MIN_NORM_TIME || normTime > MAX_NORM_TIME) {
                throw new Exception("Время может быть в диапазоне от " + MIN_NORM_TIME + " до " + MAX_NORM_TIME);
            }
        } catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        return normTime;
    }


    public static void readShortDaysAndHolidays(Map<Integer, Integer> shortDaysAndHolidays, int dayOfMonth) {
        try {
            FileInputStream fis = new FileInputStream("./userForm/userForm.xls");
            Workbook wb = new HSSFWorkbook(fis);
            for(int indexRow = ROW_INDICATES_SHORT_DAY; indexRow <= ROW_INDICATES_DAY_OFF; ++indexRow) {
                for(int indexCol = COL_INDICATES_DATE; indexCol <= dayOfMonth; ++indexCol) {
                    int indexDay = (int) wb.getSheetAt(0).getRow(indexRow).getCell(indexCol).getNumericCellValue();
                    if(indexDay == 0) break;
                    if(dateIsCorrect(indexDay, dayOfMonth, shortDaysAndHolidays)) {
                        shortDaysAndHolidays.put(indexDay, indexRow - AMOUNT_OF_HEADER);
                    }
                }
            }
            wb.close();
            fis.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(0);
        }
    }


    private static boolean dateIsCorrect(int indexDay, int dayOfMonth, final Map<Integer, Integer> shortDaysAndHolidays) throws Exception {
        if(indexDay < 0 || indexDay > dayOfMonth) {
            throw new Exception("Дата может быть в диапазоне от 1 до " + dayOfMonth);
        }
        if(shortDaysAndHolidays.get(indexDay) != null) {
            throw new Exception(indexDay + "-е число не может быть указано дважды");
        }
        return true;
    }
}