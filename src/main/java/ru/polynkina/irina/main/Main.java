package ru.polynkina.irina.main;

import ru.polynkina.irina.hours.*;
import ru.polynkina.irina.period.*;
import ru.polynkina.irina.regions.*;
import ru.polynkina.irina.fileInteraction.*;
import ru.polynkina.irina.graphs.*;
import static java.lang.Math.abs;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            ReportingPeriod period = new UserPeriod();
            Hours libHours = new LibHours();
            RegionsContainer regions = new RegionsContainer();

            List<DayGraph> graphs = new ArrayList<DayGraph>();
            LibraryReader.createGraphsOnRules(graphs);
            LibraryReader.readCountersForGraphs(graphs, period);

            for(DayGraph graph : graphs)
                graph.startGenerating(period, libHours);

            LibraryEditor.writeGraphsIntoTemplate(graphs, regions, period);
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