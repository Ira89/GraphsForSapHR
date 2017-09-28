package ru.polynkina.irina.regions;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.io.FileInputStream;
import java.util.*;

/**
 * <br> Список регионов для генерации получаем от GUI </br>
 * <br> Список графиков для каждого региона берется из папки regions (значения не изменяются) </br>
 * <br> Если файлы в папке regions отсутствуют - возбуждается исключение типа Exception </br>
 */
public class RegionsContainer {

    private List<Region> allUserRegions = new ArrayList<Region>();
    private List<String> graphsInRegion = new ArrayList<String>();


    public RegionsContainer(JCheckBox[] containers) throws Exception {
        for(JCheckBox region : containers) {
            graphsInRegion.clear();
            readGraphsInRegions(region.getText());
            allUserRegions.add(new UserRegion(region.getText(), region.isSelected(), graphsInRegion));
        }
    }


    public RegionsContainer(JCheckBox region, List<String> graphs) throws Exception {
        allUserRegions.add(new UserRegion(region.getText(), region.isSelected(), graphs));
    }


    public int getAmountRegions() {
        return allUserRegions.size();
    }


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