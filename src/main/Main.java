package ru.polynkina.irina.main;

import ru.polynkina.irina.fileInteraction.LibraryEditor;
import ru.polynkina.irina.fileInteraction.LibraryReader;
import ru.polynkina.irina.userInteraction.UserForm;
import ru.polynkina.irina.calendar.Calendar;
import ru.polynkina.irina.graphs.DayGraph;
import java.util.*;

public class Main {

    public static void main(String[] needRunningTests) {
        try {
            final int YEAR = UserForm.readYear();
            final int MONTH = UserForm.readMonth();
            final int DAYS_IN_MONTH = Calendar.getDaysInMonth(MONTH, YEAR);
            final double NORM_TIME = UserForm.readNormTime();

            List<DayGraph> graphs = new ArrayList<DayGraph>();
            LibraryReader.createGraphsOnRules(graphs);
            LibraryReader.readCountersForGraphs(graphs, MONTH, YEAR);

            Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();
            UserForm.readShortAndHolidays(shortAndHolidays, DAYS_IN_MONTH);
            Map<Double, String> dayHours = new HashMap<Double, String>();
            LibraryReader.readDayHours(dayHours);
            Map<Double, String> nightHours = new HashMap<Double, String>();
            LibraryReader.readNightHours(nightHours);

            for(DayGraph graph : graphs)
                graph.startGenerating(NORM_TIME, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

            Map<String, Integer> regions = new HashMap<String, Integer>();
            UserForm.readRegions(regions);
            for(Map.Entry<String, Integer> region : regions.entrySet()) {
                if(region.getValue() == 0) continue;
                List<String> graphsInRegions = new ArrayList<String>();
                LibraryReader.readGraphsInRegions(region.getKey(), graphsInRegions);
                LibraryEditor.writeGraphsIntoTemplate(graphs, region.getKey(), graphsInRegions, MONTH, YEAR);
            }

            LibraryEditor.writeWorkHoursInFile(graphs, MONTH, YEAR);
            LibraryEditor.writeNextCounter(graphs, DAYS_IN_MONTH, MONTH, YEAR);
            LibraryEditor.deleteOldCounter(MONTH, YEAR);

            System.out.println("Генерация графиков завершена успешно!");
            System.out.println("Версия программы: 2.0.1");

        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }
}