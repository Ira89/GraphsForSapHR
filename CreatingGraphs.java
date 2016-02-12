package ru.polynkina.irina.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatingGraphs {

    final static int MAX_DEBUG_LEVEL = 5;
    final static int DEBUG_USER_INPUT = 0;
    final static int DEBUG_READ_GRAPHS = 1;
    final static int DEBUG_GENERATION_TIME = 2;
    final static int WRITTEN_WORK_TIME = 3;
    final static int DEBUG_READ_HOURS = 4;
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

        Map<Double, String> dayHours = new HashMap<Double, String>();
        FileInteraction.readDayHours(dayHours);
        Map<Double, String> nightHours = new HashMap<Double, String>();
        FileInteraction.readNightHours(nightHours);

        FileInteraction.writeGraphsIntoTemplate(graphs, dayHours, nightHours, shortDayAndHolidays, AMOUNT_DAY);

        if(MAX_DEBUG_LEVEL == debugLevel[0].length()){
            String commandLine = debugLevel[0];
            if(commandLine.charAt(DEBUG_USER_INPUT) == ACTIVATED_DEBUG){
                Debug.printDayMonthAndYear(AMOUNT_DAY, MONTH, YEAR);
                Debug.printNormTime(NORM_TIME);
                Debug.printShortDayAndHoliday(shortDayAndHolidays);
            }
            if(commandLine.charAt(DEBUG_READ_GRAPHS) == ACTIVATED_DEBUG){
                Debug.printInfoAboutGraphs(graphs);
            }
            if(commandLine.charAt(DEBUG_GENERATION_TIME) == ACTIVATED_DEBUG){
                Debug.printInfoAboutWorkTime(graphs, AMOUNT_DAY);
            }
            if(commandLine.charAt(WRITTEN_WORK_TIME) == ACTIVATED_DEBUG){
                FileInteraction.writeWorkTimeInFile(graphs, AMOUNT_DAY);
            }
            if(commandLine.charAt(DEBUG_READ_HOURS) == ACTIVATED_DEBUG){
                Debug.printHours(dayHours);
                Debug.printHours(nightHours);
            }
        }

        System.out.println("Генерация графиков завершена!");
    }
}