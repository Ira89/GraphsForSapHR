package ru.polynkina.irina.period;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;

public class UserPeriod implements ReportingPeriod {

    private final static int[] DAYS_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final static int FEBRUARY_INDEX = 2;

    private final static int MIN_INDEX_YEAR = 2017;
    private final static int MAX_INDEX_YEAR = 2117;
    private final static int MIN_INDEX_MONTH = 1;
    private final static int MAX_INDEX_MONTH = 12;
    private final static int MIN_NORM_TIME = 100;
    private final static int MAX_NORM_TIME = 200;

    private final static String PATH_TO_USER_FILE = "./_files/userForm/userForm.xls";
    private final static int COL_INDICATES_DATE = 1;
    private final static int ROW_INDICATES_YEAR = 0;
    private final static int ROW_INDICATES_MONTH = 1;
    private final static int ROW_INDICATES_NORM_TIME = 2;

    private int year;
    private int month;
    private int daysInMonth;
    private double normTime;

    public UserPeriod() {
        initializingField();
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDaysInMonth() { return daysInMonth; }
    public double getNormTime() { return normTime; }

    private void initializingField() {
        try{
            year = takeCorrectYear(PATH_TO_USER_FILE, ROW_INDICATES_YEAR, COL_INDICATES_DATE);
            month = takeCorrectMonth(PATH_TO_USER_FILE, ROW_INDICATES_MONTH, COL_INDICATES_DATE);
            daysInMonth = getDaysInMonth(month, year);
            normTime = takeCorrectNormTime(PATH_TO_USER_FILE, ROW_INDICATES_NORM_TIME, COL_INDICATES_DATE);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    private int takeCorrectYear(String pathFile, int indexRow, int indexColumn) throws Exception {
        int year = (int) readUserData(pathFile, indexRow, indexColumn);
        if(year < MIN_INDEX_YEAR || year > MAX_INDEX_YEAR) {
            throw new Exception("Год должен быть в диапазоне: " + MIN_INDEX_YEAR + " - " + MAX_INDEX_YEAR);
        }
        return year;
    }

    private int takeCorrectMonth(String pathFile, int indexRow, int indexColumn) throws Exception {
        int month = (int) readUserData(pathFile, indexRow, indexColumn);
        if(month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH) {
            throw new Exception("Месяц должен быть в диапазоне: " + MIN_INDEX_MONTH + " - " + MAX_INDEX_MONTH);
        }
        return month;
    }

    private double takeCorrectNormTime(String pathFile, int indexRow, int indexColumn) throws Exception {
        double normTime = readUserData(pathFile, indexRow, indexColumn);
        if(normTime < MIN_NORM_TIME || normTime > MAX_NORM_TIME) {
            throw new Exception("Рабочее время должно быть в диапазоне: " + MIN_NORM_TIME + " - " + MAX_NORM_TIME);
        }
        return normTime;
    }

    private double readUserData(String pathFile, int indexRow, int indexColumn) throws Exception {
        FileInputStream fis = new FileInputStream(pathFile);
        Workbook wb = new HSSFWorkbook(fis);
        double userValue = (int) wb.getSheetAt(0).getRow(indexRow).getCell(indexColumn).getNumericCellValue();
        wb.close();
        fis.close();
        return userValue;
    }

    private int getDaysInMonth(int month, int year) {
        int daysInMonth = DAYS_IN_MONTH[month];
        if(month == FEBRUARY_INDEX) {
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) ++daysInMonth;
        }
        return daysInMonth;
    }
}
