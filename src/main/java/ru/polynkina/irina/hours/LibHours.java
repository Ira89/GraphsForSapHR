package ru.polynkina.irina.hours;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <br> Инициализация объекта происходит при создании экземпляра </br>
 * <br> Значения для инициалазиции берутся из файлов dayHours.xls и nightHours.xls </br>
 * <br> Содержимое файлов dayHours.xls и nightHours.xls не изменяется </br>
 * <br> Если один из файлов отсутствует - возбуждается исключение типа Exception </br>
 * <br> Если искать недопустимые значения в findSignDayHours или findSignNightHours - возбуждается исключение типа Exception </br>
 * <br> Допустимыми значениями являются: </br>
 *      <br> для findSignDayHours - от 0 включительно до 15 включительно </br>
 *      <br> для findSignNightHours - от 0 включительно до 22 включительно </br>
 */
public class LibHours implements Hours {

    private final static int COL_INDICATES_HOUR = 0;
    private final static int COL_INDICATES_HOUR_NAME = 1;
    private final static String PATH_TO_LIB_DAY_HOURS = "./_files/library/dayHours.xls";
    private final static String PATH_TO_LIB_NIGHT_HOURS = "./_files/library/nightHours.xls";

    private Map<Double, String> dayHours = new HashMap<Double, String>();
    private Map<Double, String> nightHours = new HashMap<Double, String>();

    public LibHours() throws Exception {
        initializingFields();
    }

    public String findSignDayHours(double time) throws Exception {
        String signHourName = dayHours.get(time);
        if(signHourName == null) {
            throw new Exception("Не найден дневной график на " + time + " час(ов)");
        }
        return signHourName;
    }

    public String findSignNightHours(double time) throws Exception {
        String signHourName = nightHours.get(time);
        if(signHourName == null) {
            throw new Exception("Не найден ночной график на " + time + " час(ов)");
        }
        return signHourName;
    }

    private void initializingFields() throws Exception {
        readLibHours(PATH_TO_LIB_DAY_HOURS, dayHours);
        readLibHours(PATH_TO_LIB_NIGHT_HOURS, nightHours);
    }

    // При возникновении NullPointerException чтение из файла завершается
    // NullPointerException игнорируется, т.к. его появление указывает на то, что мы прочитали все данные
    private void readLibHours(String pathFile, Map<Double, String> hours) throws Exception {
        FileInputStream fis = new FileInputStream(pathFile);
        Workbook wb = new HSSFWorkbook(fis);

        int indexRow = 0;
        while (true) {
            try {
                double hour = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_HOUR).getNumericCellValue();
                String hourName = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_HOUR_NAME).getStringCellValue();
                hours.put(hour, hourName);
                ++indexRow;
            } catch (NullPointerException exc) {
                break;
            }
        }

        wb.close();
        fis.close();
    }
}