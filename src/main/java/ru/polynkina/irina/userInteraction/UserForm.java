package ru.polynkina.irina.userInteraction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import ru.polynkina.irina.period.ReportingPeriod;

import java.io.FileInputStream;
import java.util.Map;

public class UserForm {

    private final static int ROW_INDICATES_SHORT_DAY = 3;
    private final static int ROW_INDICATES_DAY_OFF = 5;

    private final static int COL_INDICATES_DATE = 1;
    private final static int AMOUNT_OF_HEADERS = 3;

    public static void readShortAndHolidays(Map<Integer, Integer> shortAndHolidays, ReportingPeriod period) throws Exception {
        FileInputStream fis = new FileInputStream("./_files/userForm/userForm.xls");
        Workbook wb = new HSSFWorkbook(fis);
        for(int indexRow = ROW_INDICATES_SHORT_DAY; indexRow <= ROW_INDICATES_DAY_OFF; ++indexRow) {
            for(int indexCol = COL_INDICATES_DATE; indexCol <= period.getDaysInMonth(); ++indexCol) {
                try {
                    int indexDay = (int) wb.getSheetAt(0).getRow(indexRow).getCell(indexCol).getNumericCellValue();
                    if(indexDay == 0) break;
                    if(dateIsCorrect(indexDay, period.getDaysInMonth(), shortAndHolidays))
                        shortAndHolidays.put(indexDay, indexRow - AMOUNT_OF_HEADERS);
                } catch (NullPointerException nullExc) { break; }

            }
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