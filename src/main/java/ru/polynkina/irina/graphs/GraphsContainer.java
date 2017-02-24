package ru.polynkina.irina.graphs;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;
import ru.polynkina.irina.regions.RegionsContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static ru.polynkina.irina.graphs.Graph.*;

/**
 * <br> При создании экземпляра класса создается массив объектов Graph </br>
 * <br> Инициализация каждого объекта Graph происходит при создании экземпляра класса GraphsContainer </br>
 * <br> Для инициализации берутся значения: </br>
 *      <br> из файла rules.xls (значения данного файла не изменяются) </br>
 *      <br> из файла counter_month_year.xls (при генерации графиков создается файл для следующего месяца) </br>
 * <br> Если один из файлов отсутствует - возбуждается исключение типа Exception </br>
 */
public class GraphsContainer {

    private List<Graph> graphs = new ArrayList<Graph>();

    /**
     * Инициализирует графики значениями из шаблона
     * И устанавливает счетчики для каждого графика
     */
    public GraphsContainer(ReportingPeriod period) throws Exception {
        createGraphsOnRules();
        readCounterForGraphs(period);
    }

    /**
     * Запускает генерацию по всем графикам, находящимся в шаблоне
     * И записывает новый счетчик (для следующего месяца)
     */
    public void startGenerating(ReportingPeriod period, Hours libHours) throws Exception {
        for(Graph graph : graphs) {
            graph.startGenerating(period, libHours);
        }
        writeNextCounter(period);
    }

    /**
     * Записывает сгенерированные графики в шаблон для SAP HR (сигнатуры графиков) и в пользовательский шаблон (часы)
     */
    public void writeGraphsInFile(ReportingPeriod period, RegionsContainer regions) throws Exception {
        writeWorkHoursInFile(period);
        writeGraphsInTemplates(period, regions);
    }


    /**
     * Удаляет счетчик за предыдущий год
     */
    public void deleteOldCounter(ReportingPeriod period) {
        final String PATH_TO_COUNTERS = "./_files/counters/counter_";
        File oldFile = new File(PATH_TO_COUNTERS + period.getMonth() + "_" + (period.getYear() - 1) + ".xls");
        if(oldFile.delete()) {
            System.out.println("Удален счетчик за " + period.getMonth() + "." + (period.getYear() - 1));
        }
    }

    /**
     * Если месячная норма отличается от фактически сгенерированных часов - в консоль будет выведено предупреждение
     */
    public void printCheckResults() throws Exception {
        boolean graphIsCorrect = true;
        for(Graph graph : graphs) {
            double plannedTime = graph.getNormTime();
            double actualTime = graph.calcRealNormTime();
            if(Math.abs(plannedTime - actualTime) > 0.001) {
                System.out.println("ВНИМАНИЕ!");
                System.out.println("Для графика " + graph.getName() + " норма времени должна быть: " + plannedTime);
                System.out.println(" Фактические часы составляют: " + actualTime);
                System.out.println();
                graphIsCorrect = false;
            }
        }
        if(graphIsCorrect) System.out.println("Генерация графиков завершена успешно!");
    }


    // При возникновении NullPointerException чтение из файла завершается
    // NullPointerException игнорируется, т.к. его появление указывает на то, что мы прочитали все данные
    private void createGraphsOnRules() throws Exception {
        final String PATH_TO_RULES_GRAPH = "./_files/rules/rules.xls";
        final int COL_INDICATES_ID = 0;
        final int COL_INDICATES_NAME = 1;
        final int COL_INDICATES_RULE = 2;
        final int COL_INDICATES_TYPE = 3;
        final int COL_INDICATES_TIME = 4;
        final int COL_INDICATES_TIME_SIGN = 5;
        final int COL_INDICATES_ADD_TIME = 6;
        final int COL_INDICATES_ADD_TIME_SIGN = 7;
        final  int COL_INDICATES_TEXT = 8;

        FileInputStream fis = new FileInputStream(PATH_TO_RULES_GRAPH);
        Workbook wb = new HSSFWorkbook(fis);
        int indexRow = 0;
        while(true) {
            try {
                int id = (int) wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_ID).getNumericCellValue();
                if(id != indexRow) throw new Exception("Файл: " + PATH_TO_RULES_GRAPH + " поврежден!");
                String name = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_NAME).getStringCellValue();
                String rule = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_RULE).getStringCellValue();
                String type = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_TYPE).getStringCellValue();
                double time = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_TIME).getNumericCellValue();
                String sign = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_TIME_SIGN).getStringCellValue();
                String text = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_TEXT).getStringCellValue();
                double addTime = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_ADD_TIME).getNumericCellValue();
                String addSign = wb.getSheetAt(0).getRow(indexRow).getCell(COL_INDICATES_ADD_TIME_SIGN).getStringCellValue();
                graphs.add(createGraph(id, name, rule, type, time, sign, text, addTime, addSign));
                ++indexRow;
            } catch (NullPointerException exc) {
                break;
            }
        }
        wb.close();
        fis.close();
    }


    private Graph createGraph(int id, String name, String rule, String type, double time, String sign, String text,
                             double addTime, String addSign) throws Exception {
        if(type.equals(DAY_TYPE)) return new DayGraph(id, name, rule, time, sign, text);
        else if(type.equals(SHORT_TYPE)) return new ShortGraph(id, name, rule, time, sign, text);
        else if(type.equals(FIVE_DAY_TYPE)) return new FiveDayGraph(id, name, rule, time, sign, text);
        else if(type.equals(FRACTIONAL_TYPE)) return new FractionalGraph(id, name, rule, time, sign, text);
        else if(type.equals(DIURNAL_TYPE)) return new DiurnalGraph(id, name, rule, time, sign, text);
        else if(type.equals(UNIQUE_TYPE)) return new UniqueGraph(id, name, rule, time, sign, text, addTime, addSign);
        else return new MixedGraph(id, name, rule, time, sign, text, addTime, addSign);
    }


    private void readCounterForGraphs(ReportingPeriod period) throws Exception {
        final String PATH_TO_COUNTERS = "./_files/counters/counter_";
        final int COL_INDEX_ID = 0;
        final int COL_INDEX_VALUE = 1;

        try {
            String filename = PATH_TO_COUNTERS + period.getMonth() + "_" + period.getYear() + ".xls";
            FileInputStream fis = new FileInputStream(filename);
            Workbook wb = new HSSFWorkbook(fis);
            for(Graph graph : graphs) {
                int idGraph = graph.getId();
                if(idGraph != wb.getSheetAt(0).getRow(idGraph).getCell(COL_INDEX_ID).getNumericCellValue()) {
                    wb.close();
                    throw new Exception("Файл " + filename + " поврежден!");
                }
                int counter = (int) wb.getSheetAt(0).getRow(idGraph).getCell(COL_INDEX_VALUE).getNumericCellValue();
                graph.setCounter(counter);
            }
            wb.close();
            fis.close();
        } catch (FileNotFoundException exc) {
            throw new Exception("Не сгенерирован график за прошлый месяц!");
        }
    }


    private void writeNextCounter(ReportingPeriod period) throws Exception {
        final String PATH_TO_COUNTERS = "./_files/counters/counter_";
        final int COL_INDEX_ID = 0;
        final int COL_INDEX_VALUE = 1;

        String filename = PATH_TO_COUNTERS + period.getMonth() + "_" + period.getYear() + ".xls";
        FileInputStream fis = new FileInputStream(filename);
        Workbook wb = new HSSFWorkbook(fis);
        for (Graph graph : graphs) {
            int nextCounter = (period.getDaysInMonth() + graph.getCounter()) % graph.getLengthRule();
            Row row = wb.getSheetAt(0).getRow(graph.getId());
            Cell cell = row.createCell(COL_INDEX_ID);
            cell.setCellValue(graph.getId());
            cell = row.createCell(COL_INDEX_VALUE);
            cell.setCellValue(nextCounter);
        }
        int newMonth = period.getNextMonth();
        int year = period.getYearAfterIncreaseMonth();
        FileOutputStream fos = new FileOutputStream(PATH_TO_COUNTERS + newMonth + "_" + year + ".xls");
        wb.write(fos);
        fos.close();
        wb.close();
        fis.close();
    }


    private void writeGraphsInTemplates(ReportingPeriod period, RegionsContainer regions) throws Exception {
        final String PATH_TO_TEMPLATE = "./_files/templates/templateForSapHR.xlsx";
        final int AMOUNT_ROW_IN_HEADER = 1;
        final int COL_INDICATES_TEXT = 0;
        final int COL_INDICATES_NAME_GRAPH = 1;
        final int COL_INDICATES_NORM_TIME = 2;
        final int COL_INDICATES_HOURS = 3;
        final int SIZE_STEP = 5;
        final int OFFSET_FOR_HOLIDAYS = 2;
        final int OFFSET_FOR_SHORT_DAYS = 3;

        for(int idRegion = 0; idRegion < regions.getAmountRegions(); ++idRegion) {
            if(!regions.generationNeededForRegion(idRegion)) continue;

            FileInputStream fis = new FileInputStream(PATH_TO_TEMPLATE);
            Workbook wb = new XSSFWorkbook(fis);

            CellStyle styleForDaytime = wb.createCellStyle();
            styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            CellStyle styleForNightTime = wb.createCellStyle();
            styleForNightTime.setFillPattern(CellStyle.SOLID_FOREGROUND);
            styleForNightTime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

            int indexRow = 0;
            for(Graph graph : graphs) {
                if(!regions.graphUsedInRegion(idRegion, graph.getName())) continue;
                Row row = wb.getSheetAt(0).createRow(indexRow + AMOUNT_ROW_IN_HEADER);
                Cell cell = row.createCell(COL_INDICATES_TEXT);
                cell.setCellValue(graph.getText());
                cell = row.createCell(COL_INDICATES_NAME_GRAPH);
                cell.setCellValue(graph.getName());
                cell = row.createCell(COL_INDICATES_NORM_TIME);
                cell.setCellValue(graph.getNormTime());

                for(int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {

                    // записываем сигнатуру рабочего времени
                    cell = row.createCell(COL_INDICATES_HOURS + indexDay * SIZE_STEP);
                    cell.setCellValue(graph.getWorkTimeSign(indexDay));

                    // если день не выходной - применяем заливку цветом
                    if(isWorkingDay(graph.getId(), indexDay)) {
                        if(isNightTime(graph.getId(), indexDay)) cell.setCellStyle(styleForNightTime);
                        else cell.setCellStyle(styleForDaytime);
                    }

                    // устанавливаем признак короткого дня
                    if(graph.getShortDaysSign(indexDay) != ' ') {
                        cell = row.createCell(COL_INDICATES_HOURS + indexDay * SIZE_STEP + OFFSET_FOR_SHORT_DAYS);
                        cell.setCellValue(graph.getHolidaysSign(indexDay));
                    }

                    // устанавливаем признак выходного дня
                    if(graph.getHolidaysSign(indexDay) != ' ') {
                        cell = row.createCell(COL_INDICATES_HOURS + indexDay * SIZE_STEP + OFFSET_FOR_HOLIDAYS);
                        cell.setCellValue(graph.getHolidaysSign(indexDay));
                    }
                }
                ++indexRow;
            }
            String pathFile = "./" + regions.getNameRegion(idRegion) + "_" + period.getMonth() + "_" + period.getYear();
            FileOutputStream fos = new FileOutputStream(pathFile + ".xlsx");
            wb.write(fos);
            fos.close();
            wb.close();
            fis.close();
        }
    }


    private void writeWorkHoursInFile(ReportingPeriod period) throws Exception {
        final String PATH_TO_TEMPLATE = "./_files/templates/templateWorkingTime.xls";
        final int AMOUNT_ROW_IN_HEADER = 1;
        final int COL_INDICATES_NAME = 0;
        final int COL_INDICATES_NORM_TIME = 1;
        final int COL_INDICATES_HOUR = 2;

        FileInputStream fis = new FileInputStream(PATH_TO_TEMPLATE);
        Workbook wb = new HSSFWorkbook(fis);

        CellStyle styleForDaytime = wb.createCellStyle();
        styleForDaytime.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleForDaytime.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        CellStyle styleForNightTime = wb.createCellStyle();
        styleForNightTime.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleForNightTime.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        for(Graph graph : graphs) {
            Row row = wb.getSheetAt(0).createRow(graph.getId() + AMOUNT_ROW_IN_HEADER);
            Cell cell = row.createCell(COL_INDICATES_NAME);
            cell.setCellValue(graph.getName());
            cell = row.createCell(COL_INDICATES_NORM_TIME);
            cell.setCellValue(graph.getName());
            for (int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
                if(graph.getWorkTime(indexDay) != 0) {
                    cell = row.createCell(COL_INDICATES_HOUR + indexDay);
                    cell.setCellValue(graph.getWorkTime(indexDay));
                    if(isNightTime(graph.getId(), indexDay)) cell.setCellStyle(styleForNightTime);
                    else cell.setCellStyle(styleForDaytime);
                }
            }
        }
        String pathFile = "./workHours_" + period.getMonth() + "_" + period.getYear();
        FileOutputStream fos = new FileOutputStream(pathFile + ".xls");
        wb.write(fos);
        fos.close();
        wb.close();
        fis.close();
    }


    private boolean isWorkingDay(int idGraph, int indexDay) throws Exception {
        return graphs.get(idGraph).getWorkTime(indexDay) != 0;
    }


    private boolean isNightTime(int idGraph, int indexDay) throws Exception {
        return graphs.get(idGraph).getRuleOfDay(indexDay) == NIGHT;
    }
}
