package ru.polynkina.irina.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatingGraphs {

    final static int MAX_DEBUG_LEVEL = 4;
    final static int DEBUG_USER_DATA = 0;
    final static int DEBUG_FILE_DATA = 1;
    final static char ACTIVATED_DEBUG = '1';

    public static void main(String[] debugLevel) {
        System.out.println("Начинаю генерацию графиков!");

        int yearAndMonth = UserInteraction.readYearAndMonth();
        final int YEAR = yearAndMonth >> UserInteraction.BITS_IN_MONTH;
        final int MONTH = yearAndMonth & UserInteraction.MASK_FOR_MONTH;
        final int AMOUNT_DAY = Calendar.getAmountDay(MONTH, YEAR);
        final double NORM_TIME = UserInteraction.readNormTime();

        Map<Integer, Integer> shortDayAndHolidays = new HashMap<Integer, Integer>();
        UserInteraction.readShortDayAndHolidays(shortDayAndHolidays, AMOUNT_DAY);

        List<Graph> graphs = new ArrayList<Graph>();
        FileInteraction.fabricateGraphs(graphs);
        FileInteraction.readCountersForGraphs(graphs, MONTH, YEAR);

        if(MAX_DEBUG_LEVEL == debugLevel[0].length()){
            String commandLine = debugLevel[0];
            if(commandLine.charAt(DEBUG_USER_DATA) == ACTIVATED_DEBUG){
                Debug.printDayMonthAndYear(AMOUNT_DAY, MONTH, YEAR);
                Debug.printNormTime(NORM_TIME);
                Debug.printShortDayAndHoliday(shortDayAndHolidays);
            }
            if(commandLine.charAt(DEBUG_FILE_DATA) == ACTIVATED_DEBUG){
                Debug.printInfoAboutGraphs(graphs);
            }
        }

        System.out.println("Генерация графиков завершена!");
    }
}