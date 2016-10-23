package ru.polynkina.irina.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatingGraphs {

    static final int YEAR = UserInteraction.readYear();
    static final int MONTH = UserInteraction.readMonth();
    static final int AMOUNT_OF_DAYS = Calendar.getAmountDay(MONTH, YEAR);
    static final double NORM_TIME = UserInteraction.readNormTime();

    public static void main(String[] debugLevel) {
        System.out.println("Version of the program: 1.0.1");

        Map<Integer, Integer> shortDayAndHolidays = new HashMap<Integer, Integer>();
        UserInteraction.readShortDayAndHolidays(shortDayAndHolidays);

        List<Graph> graphs = new ArrayList<Graph>();
        FileInteraction.fabricateGraphs(graphs);
        FileInteraction.readCountersForGraphs(graphs);

        for(Graph obj : graphs){
            obj.createUninitializedWorkTimeArray();
            obj.setWeekend();
            obj.setShortDayAndHolidays(shortDayAndHolidays);

            int amountUninitializedDays = obj.getAmountUninitializedDays();
            double sumTimeInitializedDays = obj.getSumTimeInitializedDays();
            double sumTimesUninitializedDays = obj.getWorkTimeInMonth() - sumTimeInitializedDays;
            obj.generateGraph(amountUninitializedDays, sumTimesUninitializedDays);
        }

        Map<Double, String> dayHours = new HashMap<Double, String>();
        FileInteraction.readDayHours(dayHours);
        Map<Double, String> nightHours = new HashMap<Double, String>();
        FileInteraction.readNightHours(nightHours);

        FileInteraction.writeGraphsIntoTemplate(graphs, dayHours, nightHours, shortDayAndHolidays);
        FileInteraction.writeNextCounter(graphs);
        FileInteraction.deleteOldCounter();

        if (debugLevel.length == 1) Debug.start(debugLevel[0], shortDayAndHolidays, graphs, dayHours, nightHours);
        System.out.println("Генерация графиков завершена успешно!");
    }
}