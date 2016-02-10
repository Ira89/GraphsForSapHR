package ru.polynkina.irina.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatingGraphs {

    final static int MAX_DEBUG_LEVEL = 6;
    final static int DEBUG_USER_DATA = 0;
    final static int DEBUG_FILE_DATA = 1;
    final static int DEBUG_GENERATION_TIME = 2;
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

        for(Graph obj : graphs){
            obj.createUninitializedWorkTimeArray(AMOUNT_DAY);
            obj.setWeekend(AMOUNT_DAY);
            obj.setShortDayAndHolidays(AMOUNT_DAY, shortDayAndHolidays);

            int amountUninitializedDays = obj.getAmountUninitializedDays(AMOUNT_DAY);
            double sumTimeInitializedDays = obj.getSumTimeInitializedDays(AMOUNT_DAY);
            double sumTimesUninitializedDays = NORM_TIME - sumTimeInitializedDays;
            obj.generateGraph(AMOUNT_DAY, amountUninitializedDays, sumTimesUninitializedDays);
        }

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
            if(commandLine.charAt(DEBUG_GENERATION_TIME) == ACTIVATED_DEBUG){
                Debug.printInfoAboutWorkTime(graphs, AMOUNT_DAY);
            }
        }

        System.out.println("Генерация графиков завершена!");
    }
}