package ru.polynkina.irina.regions;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.util.*;

/**
 * <br> При создании экземпляра класса создается массив объектов UserRegion </br>
 * <br> Инициализация каждого объекта UserRegion происходит при создании экземпляра класса </br>
 * <br> Для инициализации берутся значения: </br>
 *      <br> из файла userForm.xls (значения данного файла изменяются пользователем) </br>
 *      <br> из папки regions (значения не изменяются) </br>
 * <br> Если один из файлов отсутствует - возбуждается исключение типа Exception </br>
 */
public class RegionsContainer {

    private List<Region> allUserRegions = new ArrayList<Region>();
    private Map<String, Boolean> regions = new HashMap<String, Boolean>();
    private List<String> graphsInRegion = new ArrayList<String>();


    public RegionsContainer() throws Exception {
        readUserRegions();
        for(Map.Entry<String, Boolean> region : regions.entrySet()) {
            graphsInRegion.clear();
            readGraphsInRegions(region.getKey());
            allUserRegions.add(new UserRegion(region.getKey(), region.getValue(), graphsInRegion));
        }
    }

    public int getAmountRegions() { return allUserRegions.size(); }


    public String getNameRegion(int indexRegion) {
        return allUserRegions.get(indexRegion).getNameRegion();
    }


    public boolean generationNeededForRegion(int indexRegion) {
        return allUserRegions.get(indexRegion).generationNeededForRegion();
    }


    public boolean graphUsedInRegion(int indexRegion, String nameGraphs) {
        return allUserRegions.get(indexRegion).graphIsUsedInRegion(nameGraphs);
    }


    // При возникновении NullPointerException чтение из файла завершается
    // NullPointerException игнорируется, т.к. его появление указывает на то, что мы прочитали все данные
    private void readUserRegions() throws Exception {
        final String PATH_TO_USER_FILE = "./_files/userForm/userForm.xls";
        final int COL_INDICATES_DATE = 1;
        final int ROW_INDICATES_REGION = 7;
        final int ROW_INDICATES_NEED_GRAPH = 8;

        FileInputStream fis = new FileInputStream(PATH_TO_USER_FILE);
        Workbook wb = new HSSFWorkbook(fis);
        int indexCol = COL_INDICATES_DATE;
        while(true) {
            try {
                String nameRegion = wb.getSheetAt(0).getRow(ROW_INDICATES_REGION).getCell(indexCol).getStringCellValue();
                int statusRegion = (int) wb.getSheetAt(0).getRow(ROW_INDICATES_NEED_GRAPH).getCell(indexCol).getNumericCellValue();
                regions.put(nameRegion, statusRegion == 1);
                ++indexCol;
            } catch (NullPointerException exc) {
                break;
            }
        }
        wb.close();
        fis.close();
    }


    // При возникновении NullPointerException чтение из файла завершается
    // NullPointerException игнорируется, т.к. его появление указывает на то, что мы прочитали все данные
    private void readGraphsInRegions(String nameRegion) throws Exception {
        final String PATH_TO_LIB_REGIONS = "./_files/regions/";

        FileInputStream fis = new FileInputStream(PATH_TO_LIB_REGIONS + nameRegion + ".xls");
        Workbook wb = new HSSFWorkbook(fis);
        int indexRow = 0;
        while(true) {
            try {
                String nameGraph = wb.getSheetAt(0).getRow(indexRow).getCell(0).getStringCellValue();
                graphsInRegion.add(nameGraph);
                ++indexRow;
            } catch (NullPointerException exc) {
                break;
            }
        }
        wb.close();
        fis.close();
    }
}