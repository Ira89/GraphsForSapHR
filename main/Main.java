package ru.polynkina.irina.main;

import ru.polynkina.irina.fileInteraction.FileInteraction;
import ru.polynkina.irina.unitTests.UnitTest;
import ru.polynkina.irina.userInteraction.UserForm;
import ru.polynkina.irina.calendar.Calendar;
import ru.polynkina.irina.graphs.Graph;
import ru.polynkina.irina.debug.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final int YEAR = UserForm.readYear();
    public static final int MONTH = UserForm.readMonth();
    public static final int DAY_OF_MONTH = Calendar.getAmountDay(MONTH, YEAR);
    public static final double NORM_TIME = UserForm.readNormTime();

    public static void main(String[] debugLevel) {
        System.out.println("Version of the program: 1.0.1");

        Map<Integer, Integer> shortDayAndHolidays = new HashMap<Integer, Integer>();
        UserForm.readShortDayAndHolidays(shortDayAndHolidays);

        List<Graph> graphs = new ArrayList<Graph>();
        FileInteraction.fabricateGraphs(graphs);
        FileInteraction.readCountersForGraphs(graphs);

        Map<Double, String> dayHours = new HashMap<Double, String>();
        FileInteraction.readDayHours(dayHours);
        Map<Double, String> nightHours = new HashMap<Double, String>();
        FileInteraction.readNightHours(nightHours);

        for(Graph obj : graphs) obj.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        FileInteraction.writeGraphsIntoTemplate(graphs, dayHours, nightHours, shortDayAndHolidays);
        FileInteraction.writeNextCounter(graphs);
        FileInteraction.deleteOldCounter();

        UnitTest test = new UnitTest();
        test.start();

        if (debugLevel.length == 1) Debug.start(debugLevel[0], shortDayAndHolidays, graphs, dayHours, nightHours); // ?!
        System.out.println("Генерация графиков завершена успешно!");
    }
}