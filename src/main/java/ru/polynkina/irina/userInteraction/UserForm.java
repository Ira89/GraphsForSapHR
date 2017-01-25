package ru.polynkina.irina.userInteraction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.util.Map;

public class UserForm {

    private final static int ROW_INDICATES_YEAR = 0;
    private final static int ROW_INDICATES_MONTH = 1;
    private final static int ROW_INDICATES_NORM_TIME = 2;
    private final static int ROW_INDICATES_SHORT_DAY = 3;
    private final static int ROW_INDICATES_DAY_OFF = 5;
    private final static int ROW_INDICATES_CALENDAR = 7;
    private final static int ROW_INDICATES_NEED_CALENDAR = 8;

    private final static int COL_INDICATES_DATE = 1;
    private final static int AMOUNT_OF_HEADERS = 3;

    private final static int MIN_INDEX_MONTH = 1;
    private final static int MAX_INDEX_MONTH = 12;
    private final static int MIN_NORM_TIME = 100;
    private final static int MAX_NORM_TIME = 200;

    public static int readYear() throws Exception {
        FileInputStream fis = new FileInputStream("./_files/userForm/userForm.xls");
        Workbook wb = new HSSFWorkbook(fis);
        int year = (int) wb.getSheetAt(0).getRow(ROW_INDICATES_YEAR).getCell(COL_INDICATES_DATE).getNumericCellValue();
        wb.close();
        fis.close();
        return year;
    }

    public static int readMonth() throws Exception {
        FileInputStream fis = new FileInputStream("./_files/userForm/userForm.xls");
        Workbook wb = new HSSFWorkbook(fis);
        int month = (int) wb.getSheetAt(0).getRow(ROW_INDICATES_MONTH).getCell(COL_INDICATES_DATE).getNumericCellValue();
        wb.close();
        fis.close();

        if (month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH)
            throw new Exception("Месяц должен быть в диапазоне от " + MIN_INDEX_MONTH + " до " + MAX_INDEX_MONTH);

        return month;
    }

    public static double readNormTime() throws Exception {
        FileInputStream fis = new FileInputStream("./_files/userForm/userForm.xls");
        Workbook wb = new HSSFWorkbook(fis);
        double normTime = wb.getSheetAt(0).getRow(ROW_INDICATES_NORM_TIME).getCell(COL_INDICATES_DATE).getNumericCellValue();
        wb.close();
        fis.close();

        if(normTime < MIN_NORM_TIME || normTime > MAX_NORM_TIME)
            throw new Exception("Время может быть в диапазоне от " + MIN_NORM_TIME + " до " + MAX_NORM_TIME);

        return normTime;
    }

    public static void readShortAndHolidays(Map<Integer, Integer> shortAndHolidays, int daysInMonth) throws Exception {
        FileInputStream fis = new FileInputStream("./_files/userForm/userForm.xls");
        Workbook wb = new HSSFWorkbook(fis);
        for(int indexRow = ROW_INDICATES_SHORT_DAY; indexRow <= ROW_INDICATES_DAY_OFF; ++indexRow) {
            for(int indexCol = COL_INDICATES_DATE; indexCol <= daysInMonth; ++indexCol) {
                try {
                    int indexDay = (int) wb.getSheetAt(0).getRow(indexRow).getCell(indexCol).getNumericCellValue();
                    if(indexDay == 0) break;
                    if(dateIsCorrect(indexDay, daysInMonth, shortAndHolidays))
                        shortAndHolidays.put(indexDay, indexRow - AMOUNT_OF_HEADERS);
                } catch (NullPointerException nullExc) { break; }

            }
        }
        wb.close();
        fis.close();
    }

    public static void readRegions(Map<String, Integer> regions) throws Exception {
        FileInputStream fis = new FileInputStream("./_files/userForm/userForm.xls");
        Workbook wb = new HSSFWorkbook(fis);
        int indexCol = COL_INDICATES_DATE;
        while(true) {
            try {
                String nameCalendar = wb.getSheetAt(0).getRow(ROW_INDICATES_CALENDAR).getCell(indexCol).getStringCellValue();
                Integer statusCalendar = (int) wb.getSheetAt(0).getRow(ROW_INDICATES_NEED_CALENDAR).getCell(indexCol).getNumericCellValue();
                if(nameCalendar.equals("")) break;
                regions.put(nameCalendar, statusCalendar);
                ++indexCol;
            } catch (NullPointerException nullExc) { break; }
        }
        wb.close();
        fis.close();
    }

    private static boolean dateIsCorrect(int indexDay, int daysInMonth, Map<Integer, Integer> shortAndHolidays) throws Exception {
        if(indexDay < 0 || indexDay > daysInMonth)
            throw new Exception("Дата может быть в диапазоне от 1 до " + daysInMonth);

        if(shortAndHolidays.get(indexDay) != null)
            throw new Exception(indexDay + "-е число не может быть указано дважды");

        return true;
    }
}