package ru.polynkina.irina.main;

import ru.polynkina.irina.graphs.GraphsContainer;
import ru.polynkina.irina.gui.UserFrame;
import ru.polynkina.irina.regions.RegionsContainer;
import ru.polynkina.irina.period.ReportingPeriod;
import ru.polynkina.irina.period.UserPeriod;
import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.hours.LibHours;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { new UserFrame(); }
        });

        try {
            ReportingPeriod period = new UserPeriod();
            Hours libHours = new LibHours();
            RegionsContainer regions = new RegionsContainer();

            GraphsContainer graphs = new GraphsContainer(period);
            graphs.startGenerating(period, libHours);
            graphs.writeGraphsInFile(period, regions);
            graphs.deleteOldCounter(period);
            graphs.printCheckResults();

            System.out.println("Версия программы: 3.1.0");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }
}