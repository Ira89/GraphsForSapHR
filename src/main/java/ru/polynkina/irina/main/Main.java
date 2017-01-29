package ru.polynkina.irina.main;

import ru.polynkina.irina.graphs.GraphsContainer;
import ru.polynkina.irina.regions.RegionsContainer;
import ru.polynkina.irina.period.ReportingPeriod;
import ru.polynkina.irina.period.UserPeriod;
import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.hours.LibHours;

public class Main {

    public static void main(String[] args) {
        try {
            ReportingPeriod period = new UserPeriod();
            Hours libHours = new LibHours();
            RegionsContainer regions = new RegionsContainer();

            GraphsContainer graphs = new GraphsContainer(period);
            graphs.startGenerating(period, libHours);
            graphs.writeGraphsInFile(period, regions);
            graphs.deleteOldCounter(period);
            graphs.printCheckResults();

            System.out.println("Версия программы: 3.0.0");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }
}