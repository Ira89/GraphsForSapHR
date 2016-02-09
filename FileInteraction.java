package ru.polynkina.irina.graphs;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileInputStream;
import java.util.List;

public class FileInteraction {

    final static int INDEX_OF_SHEET = 0;
    final static int COL_INDEX_OF_ID = 0;
    final static int COL_INDEX_OF_NAME = 1;
    final static int COL_INDEX_OF_RULE = 2;
    final static int COL_INDEX_OF_TYPE = 3;
    final static int COL_INDEX_OF_DAYTIME = 4;
    final static int COL_INDEX_OF_DAYTIME_SIGN = 5;

    final static int INDEX_COUNTER_ID = 0;
    final static int INDEX_COUNTER_VALUE = 1;

    final static String TYPE_DESIGNATION_OF_DAY = "DAY";

    public static void fabricateGraphs(List<Graph> graphs){
        try{

            FileInputStream fis = new FileInputStream("./lib/graphs.xls");
            Workbook wb = new HSSFWorkbook(fis);

            int indexRow = 0;
            while(true){
                try{

                    int id = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_ID).getNumericCellValue();
                    String name = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_NAME).getStringCellValue();
                    String rule = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_RULE).getStringCellValue();
                    String type = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_TYPE).getStringCellValue();
                    double daytime = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_DAYTIME).getNumericCellValue();
                    String daytimeSign = wb.getSheetAt(INDEX_OF_SHEET).getRow(indexRow).getCell(COL_INDEX_OF_DAYTIME_SIGN).getStringCellValue();
                    ++indexRow;

                    if(type.equals(TYPE_DESIGNATION_OF_DAY)){
                        graphs.add(new Graph(id, name, rule, daytime, daytimeSign));
                    }else{
                        graphs.add(new Graph(id, name, rule, daytime, daytimeSign));
                    }

                }catch(NullPointerException nullExc){
                    break;
                }
            }

            wb.close();
            fis.close();

        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }



    public static void readCountersForGraphs(List<Graph> graphs, int indexMonth, int indexYear){
        try{

            String filename = "counter_" + indexMonth + "_" + indexYear + ".xls";
            FileInputStream fis = new FileInputStream("./count/" + filename);
            Workbook wb = new HSSFWorkbook(fis);

            for(Graph obj : graphs){
                int idGraph = obj.getId();
                int idCounter = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(idGraph).getCell(INDEX_COUNTER_ID).getNumericCellValue();
                int valueCounter = (int) wb.getSheetAt(INDEX_OF_SHEET).getRow(idGraph).getCell(INDEX_COUNTER_VALUE).getNumericCellValue();

                if(idGraph != idCounter) throw new Exception("Вероятно, файл " + filename + " поврежден");
                obj.setCounter(valueCounter);
            }

            wb.close();
            fis.close();

        }catch(Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
    }
}