package ru.polynkina.irina.fileInteraction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import ru.polynkina.irina.graphs.*;

import java.util.Map;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LibraryReader {

    // in file rules.xls
    private final static int COL_INDICATES_ID = 0;
    private final static int COL_INDICATES_NAME = 1;
    private final static int COL_INDICATES_RULE = 2;
    private final static int COL_INDICATES_TYPE = 3;
    private final static int COL_INDICATES_BASIC_TIME = 4;
    private final static int COL_INDICATES_BASIC_TIME_SIGN = 5;
    private final static int COL_INDICATES_EXTRA_TIME = 6;
    private final static int COL_INDICATES_EXTRA_TIME_SIGN = 7;

    // in file counter_...xls
    private final static int COL_INDEX_COUNTER_ID = 0;
    private final static int COL_INDEX_COUNTER_VALUE = 1;

    // in file dayHours.xls and nightHours.xls
    private final static int COL_INDICATES_HOUR = 0;
    private final static int COL_INDICATES_HOUR_NAME = 1;

    public static void createGraphsOnRules(List<DayGraph> graphs) throws Exception {
        FileInputStream fis = new FileInputStream("./_files/rules/rules.xls");
        Workbook wb = new HSSFWorkbook(fis);

        int indexRow = 0;
        while(true) {
            try {
                int id = (int) wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_ID).getNumericCellValue();
                if (id != indexRow) throw new Exception("Файл rules.xls поврежден!");
                String name = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_NAME).getStringCellValue();
                String rule = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_RULE).getStringCellValue();
                String type = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_TYPE).getStringCellValue();
                double basicTime = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_BASIC_TIME).getNumericCellValue();
                String basicTimeSign = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_BASIC_TIME_SIGN).getStringCellValue();

                if(type.equals("DAY")) graphs.add(new DayGraph(id, name, rule, basicTime, basicTimeSign));
                else if(type.equals("SHORT")) graphs.add(new ShortGraph(id, name, rule, basicTime, basicTimeSign));
                else if(type.equals("STANDARD")) graphs.add(new FiveDayGraph(id, name, rule, basicTime, basicTimeSign));
                else if(type.equals("FLOAT")) graphs.add(new FractionalGraph(id, name, rule, basicTime, basicTimeSign));
                else if(type.equals("DIURNAL")) graphs.add(new DiurnalGraph(id, name, rule, basicTime, basicTimeSign));
                else if(type.equals("UNIQUE") || type.equals("MIX")) {
                    double extraTime = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_EXTRA_TIME).getNumericCellValue();
                    String extraTimeSign = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_EXTRA_TIME_SIGN).getStringCellValue();
                    if(type.equals("MIX")) graphs.add(new MixedGraph(id, name, rule, basicTime, basicTimeSign, extraTime, extraTimeSign));
                    else graphs.add(new UniqueGraph(id, name, rule, basicTime, basicTimeSign, extraTime, extraTimeSign));
                } else throw new Exception("Тип графика: " + type + " неизвестен!");

                ++indexRow;
            } catch (NullPointerException nullExc) { break; }
        }
        wb.close();
        fis.close();
    }

    public static void readCountersForGraphs(List<DayGraph> graphs, int month, int year) throws Exception {
        try {
            String filename = "counter_" + month + "_" + year + ".xls";
            FileInputStream fis = new FileInputStream("./_files/counters/" + filename);
            Workbook wb = new HSSFWorkbook(fis);

            for (DayGraph obj : graphs) {
                int idGraph = obj.getId();
                if(idGraph != (int) wb.getSheetAt(0).getRow(idGraph).getCell(COL_INDEX_COUNTER_ID).getNumericCellValue())
                    throw new Exception("Файл " + filename + " поврежден");
                obj.setCounter((int) wb.getSheetAt(0).getRow(idGraph).getCell(COL_INDEX_COUNTER_VALUE).getNumericCellValue());
            }

            wb.close();
            fis.close();
        } catch(FileNotFoundException excFile){ throw new Exception("Не сгенерирован график за предыдущий месяц!"); }
    }

    public static void readDayHours(Map<Double, String> dayHours) throws Exception {
        readHours("./_files/library/dayHours.xls", dayHours);
    }

    public static void readNightHours(Map<Double, String> nightHours) throws Exception {
        readHours("./_files/library/nightHours.xls", nightHours);
    }

    private static void readHours(String directory, Map<Double, String> hours) throws Exception {
        FileInputStream fis = new FileInputStream(directory);
        Workbook wb = new HSSFWorkbook(fis);

        int indexRow = 0;
        while(true) {
            try {
                double hour = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_HOUR).getNumericCellValue();
                String hourName = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_HOUR_NAME).getStringCellValue();
                hours.put(hour, hourName);
                ++indexRow;
            } catch (NullPointerException nullExc) { break; }
        }
        wb.close();
        fis.close();
    }

    public static void readGraphsInRegions(String nameRegion, List<String> graphsForRegion) throws Exception {
        FileInputStream fis = new FileInputStream("./_files/regions/" + nameRegion + ".xls");
        Workbook wb = new HSSFWorkbook(fis);

        int indexRow = 0;
        while (true) {
            try {
                String nameGraph = wb.getSheetAt(0).getRow(indexRow).getCell(0).getStringCellValue();
                if (nameGraph.equals("")) break;
                graphsForRegion.add(nameGraph);
                ++indexRow;
            } catch (NullPointerException nullExc) { break; }
        }
        wb.close();
        fis.close();
    }
}