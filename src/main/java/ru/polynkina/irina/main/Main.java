package ru.polynkina.irina.main;

import ru.polynkina.irina.hours.*;
import ru.polynkina.irina.period.*;
import ru.polynkina.irina.fileInteraction.*;
import ru.polynkina.irina.userInteraction.*;
import ru.polynkina.irina.graphs.*;
import static java.lang.Math.abs;
import java.util.*;

public class Main {

    public static void main(String[] needRunningTests) {
        try {
            ReportingPeriod period = new UserPeriod();

            List<DayGraph> graphs = new ArrayList<DayGraph>();
            LibraryReader.createGraphsOnRules(graphs);
            LibraryReader.readCountersForGraphs(graphs, period);

            Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();
            UserForm.readShortAndHolidays(shortAndHolidays, period);

            Hours libHours = new LibHours();

            for(DayGraph graph : graphs)
                graph.startGenerating(period, shortAndHolidays, libHours);

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