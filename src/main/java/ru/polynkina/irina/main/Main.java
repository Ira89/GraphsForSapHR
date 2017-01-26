package ru.polynkina.irina.main;

import ru.polynkina.irina.period.UserPeriod;
import ru.polynkina.irina.fileInteraction.LibraryEditor;
import ru.polynkina.irina.fileInteraction.LibraryReader;
import ru.polynkina.irina.userInteraction.UserForm;
import ru.polynkina.irina.graphs.DayGraph;
import static java.lang.Math.abs;
import java.util.*;

public class Main {

    public static void main(String[] needRunningTests) {
        try {
            UserPeriod period = new UserPeriod();

            List<DayGraph> graphs = new ArrayList<DayGraph>();
            LibraryReader.createGraphsOnRules(graphs);
            LibraryReader.readCountersForGraphs(graphs, period);

            Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();
            UserForm.readShortAndHolidays(shortAndHolidays, period);
            Map<Double, String> dayHours = new HashMap<Double, String>();
            LibraryReader.readDayHours(dayHours);
            Map<Double, String> nightHours = new HashMap<Double, String>();
            LibraryReader.readNightHours(nightHours);

            for(DayGraph graph : graphs)
                graph.startGenerating(period, shortAndHolidays, dayHours, nightHours);

            Map<String, Integer> regions = new HashMap<String, Integer>();
            UserForm.readRegions(regions);
            for(Map.Entry<String, Integer> region : regions.entrySet()) {
                if(region.getValue() == 0) continue;
                List<String> graphsInRegions = new ArrayList<String>();
                LibraryReader.readGraphsInRegions(region.getKey(), graphsInRegions);
                LibraryEditor.writeGraphsIntoTemplate(graphs, region.getKey(), graphsInRegions, period);
            }

            LibraryEditor.writeWorkHoursInFile(graphs, period);
            LibraryEditor.writeNextCounter(graphs, period);
            LibraryEditor.deleteOldCounter(period);

            for(DayGraph graph : graphs) {
                double plannedRateOfTime = graph.getNormTime();
                double actualRateOfTime = graph.calcRealNormTime();
                if(abs(plannedRateOfTime - actualRateOfTime) > 0.001) {
                    System.out.print("ВНИМАНИЕ! Для графика " + graph.getName() + " норма времени должна быть: " + plannedRateOfTime);
                    System.out.println(" Фактические часы составляют: " + actualRateOfTime);
                }
            }

            System.out.println("Генерация графиков завершена!");
            System.out.println("Версия программы: 2.2.1");

        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }
}