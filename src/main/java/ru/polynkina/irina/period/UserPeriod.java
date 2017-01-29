package ru.polynkina.irina.period;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <br> Инициализация объекта происходит при создании экземпляра </br>
 * <br> Значения для инициализации берутся из файла userForm.xls </br>
 * <br> Файл user.xls изменяется пользователем </br>
 * <br> Если файл отсутствует - возбуждается исключение типа Exception </br>
 * <br> Если пользователь ввел некорректное значение - возбуждается исключение типа Exception </br>
 * <br> Допустимыми значениями являются: </br>
 *      <br> год - от 2017 до 2117  </br>
 *      <br> месяц - от 1 до 12 </br>
 *      <br> норма времени - от 100 до 200 </br>
 */
public class UserPeriod implements ReportingPeriod {

    private int year;
    private int month;
    private int daysInMonth;
    private double normTime;
    private Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();


    public UserPeriod() throws Exception {
        final String PATH_TO_USER_FILE = "./_files/userForm/userForm.xls";
        final int COL_INDICATES_DATE = 1;
        final int ROW_INDICATES_YEAR = 0;
        final int ROW_INDICATES_MONTH = 1;
        final int ROW_INDICATES_NORM_TIME = 2;
        final int ROW_INDICATES_SHORT_DAY = 3;
        final int ROW_INDICATES_DAY_OFF = 5;

        year = takeYear(PATH_TO_USER_FILE, ROW_INDICATES_YEAR, COL_INDICATES_DATE);
        month = takeMonth(PATH_TO_USER_FILE, ROW_INDICATES_MONTH, COL_INDICATES_DATE);
        daysInMonth = getDaysInMonth(month, year);
        normTime = takeNormTime(PATH_TO_USER_FILE, ROW_INDICATES_NORM_TIME, COL_INDICATES_DATE);
        takeShortAndHolidays(PATH_TO_USER_FILE, ROW_INDICATES_SHORT_DAY, ROW_INDICATES_DAY_OFF, COL_INDICATES_DATE);
    }


    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDaysInMonth() { return daysInMonth; }
    public double getNormTime() { return normTime; }
    public Map<Integer, Integer> getCopyShortAndHolidays() { return new HashMap<Integer, Integer>(shortAndHolidays); }


    private int takeYear(String pathFile, int indexRow, int indexColumn) throws Exception {
        final int MIN_INDEX_YEAR = 2017;
        final int MAX_INDEX_YEAR = 2117;
        int year = (int) readUserData(pathFile, indexRow, indexColumn);
        if(year < MIN_INDEX_YEAR || year > MAX_INDEX_YEAR) {
            throw new Exception("Год должен быть в диапазоне: " + MIN_INDEX_YEAR + " - " + MAX_INDEX_YEAR);
        }
        return year;
    }


    private int takeMonth(String pathFile, int indexRow, int indexColumn) throws Exception {
        final int MIN_INDEX_MONTH = 1;
        final int MAX_INDEX_MONTH = 12;
        int month = (int) readUserData(pathFile, indexRow, indexColumn);
        if(month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH) {
            throw new Exception("Месяц должен быть в диапазоне: " + MIN_INDEX_MONTH + " - " + MAX_INDEX_MONTH);
        }
        return month;
    }


    private double takeNormTime(String pathFile, int indexRow, int indexColumn) throws Exception {
        final int MIN_NORM_TIME = 100;
        final int MAX_NORM_TIME = 200;
        double normTime = readUserData(pathFile, indexRow, indexColumn);
        if(normTime < MIN_NORM_TIME || normTime > MAX_NORM_TIME) {
            throw new Exception("Рабочее время должно быть в диапазоне: " + MIN_NORM_TIME + " - " + MAX_NORM_TIME);
        }
        return normTime;
    }


    // Если при чтение даты получили 0 - возбуждается NullPointerException
    // NullPointerException игнорируется, т.к. его появление указывает на то, что мы прочитали все данные в строке
    private void takeShortAndHolidays(String pathFile, int beginRow, int endRow, int indexColumn) throws Exception {
        final int AMOUNT_OF_HEADERS = 3;
        for(int indexRow = beginRow; indexRow <= endRow; ++indexRow) {
            while(true) {
                try {
                    int date = (int) readUserData(pathFile, indexRow, indexColumn);
                    if(date == 0) {
                        throw new NullPointerException();
                    } else if(shortAndHolidays.containsValue(date)) {
                        throw new Exception(date + "-е число не может быть указано дважды!");
                    } else if(date < 1 || date > daysInMonth) {
                        throw new Exception("Дата должна быть в диапазоне от 1 до " + daysInMonth);
                    }
                    shortAndHolidays.put(date, indexRow - AMOUNT_OF_HEADERS);
                    ++indexColumn;
                } catch (NullPointerException exc) {
                    break;
                }
            }
        }
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
        final int[] DAYS_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        final int FEBRUARY_INDEX = 2;
        int daysInMonth = DAYS_IN_MONTH[month];
        if(month == FEBRUARY_INDEX) {
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) ++daysInMonth;
        }
        return daysInMonth;
    }
}