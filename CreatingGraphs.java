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
        System.out.println("Version of the program: 1.0.0");

        int yearAndMonth = UserInteraction.readYearAndMonth();
        final int YEAR = yearAndMonth >> UserInteraction.BITS_IN_MONTH;
        final int MONTH = yearAndMonth & UserInteraction.MASK_FOR_MONTH;
        final int AMOUNT_OF_DAYS = Calendar.getAmountDay(MONTH, YEAR);
        final double NORM_TIME = UserInteraction.readNormTime();

        Map<Integer, Integer> shortDayAndHolidays = new HashMap<Integer, Integer>();
        UserInteraction.readShortDayAndHolidays(shortDayAndHolidays, AMOUNT_OF_DAYS);

        List<Graph> graphs = new ArrayList<Graph>();
        FileInteraction.fabricateGraphs(graphs, NORM_TIME);
        FileInteraction.readCountersForGraphs(graphs, MONTH, YEAR);

        for(Graph obj : graphs){
            obj.createUninitializedWorkTimeArray(AMOUNT_OF_DAYS);
            obj.setWeekend(AMOUNT_OF_DAYS);
            obj.setShortDayAndHolidays(shortDayAndHolidays, AMOUNT_OF_DAYS);

            int amountUninitializedDays = obj.getAmountUninitializedDays(AMOUNT_OF_DAYS);
            double sumTimeInitializedDays = obj.getSumTimeInitializedDays(AMOUNT_OF_DAYS);
            double sumTimesUninitializedDays = obj.getWorkTimeInMonth() - sumTimeInitializedDays;
            obj.generateGraph(amountUninitializedDays, sumTimesUninitializedDays, AMOUNT_OF_DAYS);
        }

        Map<Double, String> dayHours = new HashMap<Double, String>();
        FileInteraction.readDayHours(dayHours);
        Map<Double, String> nightHours = new HashMap<Double, String>();
        FileInteraction.readNightHours(nightHours);

        FileInteraction.writeGraphsIntoTemplate(graphs, dayHours, nightHours, shortDayAndHolidays, AMOUNT_OF_DAYS);
        FileInteraction.writeNextCounter(graphs, AMOUNT_OF_DAYS, MONTH, YEAR);
        FileInteraction.deleteOldCounter(MONTH, YEAR);

        if(MAX_DEBUG_LEVEL == debugLevel[0].length()){
            String commandLine = debugLevel[0];
            if(commandLine.charAt(DEBUG_USER_INPUT) == ACTIVATED_DEBUG){
                Debug.printDayMonthAndYear(AMOUNT_OF_DAYS, MONTH, YEAR);
                Debug.printNormTime(NORM_TIME);
                Debug.printShortDayAndHoliday(shortDayAndHolidays);
            }
            if(commandLine.charAt(DEBUG_READ_GRAPHS) == ACTIVATED_DEBUG){
                Debug.printInfoAboutGraphs(graphs);
            }
            if(commandLine.charAt(DEBUG_GENERATION_TIME) == ACTIVATED_DEBUG){
                Debug.printInfoAboutWorkTime(graphs, AMOUNT_OF_DAYS);
            }
            if(commandLine.charAt(WRITTEN_WORK_TIME) == ACTIVATED_DEBUG){
                FileInteraction.writeWorkTimeInFile(graphs, AMOUNT_OF_DAYS);
            }
            if(commandLine.charAt(DEBUG_READ_HOURS) == ACTIVATED_DEBUG){
                Debug.printHoursName(dayHours);
                Debug.printHoursName(nightHours);
            }
        }

        System.out.println("Генерация графиков завершена успешно!");
    }
}