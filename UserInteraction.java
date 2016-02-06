package ru.polynkina.irina.graphs;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;

public class UserInteraction {

    final static int MAX_INDEX_YEAR = 2116;
    final static int MIN_INDEX_YEAR = 2016;
    final static int MAX_INDEX_MONTH = 12;
    final static int MIN_INDEX_MONTH = 1;
    final static int BITS_IN_MONTH = 4;
    final static int MASK_FOR_MONTH = 0xF;


    public static int readYearAndMonth(){
        int yearAndMonth = 0;
        try{
            FileInputStream fis = new FileInputStream("./lib/userInput.xls");
            Workbook wb = new HSSFWorkbook(fis);
            int year = (int) wb.getSheetAt(0).getRow(0).getCell(1).getNumericCellValue();
            int month = (int) wb.getSheetAt(0).getRow(1).getCell(1).getNumericCellValue();

            boolean isIncorrectInput = false;
            if(year < MIN_INDEX_YEAR || year > MAX_INDEX_YEAR) isIncorrectInput = true;
            if(month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH) isIncorrectInput = true;
            if(isIncorrectInput){
                System.out.print("Некорректно указан месяц или год. ");
                System.out.println("Исправьте значение и перезапустите программу!");
                System.exit(0);
            }

            yearAndMonth = year << BITS_IN_MONTH | month;

        }catch(Exception exc){
            System.out.println("Извините, при работе с файлом userInput возникли проблемы");
        }
        return yearAndMonth;
    }
}