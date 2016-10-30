package ru.polynkina.irina.main;

import ru.polynkina.irina.fileInteraction.FileInteraction;
import ru.polynkina.irina.userInteraction.UserForm;
import ru.polynkina.irina.unitTests.UnitTest;
import ru.polynkina.irina.calendar.Calendar;
import ru.polynkina.irina.graphs.Graph;

import java.util.*;

public class Main {

    public static void main(String[] needRunningTests) {
        if(needRunningTests[0].equals("1")) {
            UnitTest test = new UnitTest();
            test.start();
        }

        final int YEAR = UserForm.readYear();
        final int MONTH = UserForm.readMonth();
        final int DAY_OF_MONTH = Calendar.getDayOfMonth(MONTH, YEAR);
        final double NORM_TIME = UserForm.readNormTime();

        Map<Integer, Integer> shortDayAndHolidays = new LinkedHashMap<Integer, Integer>();
        UserForm.readShortDaysAndHolidays(shortDayAndHolidays, DAY_OF_MONTH);

        List<Graph> graphs = new ArrayList<Graph>();
        FileInteraction.fabricateGraphs(graphs);
        FileInteraction.readCountersForGraphs(graphs, MONTH, YEAR);

        Map<Double, String> dayHours = new HashMap<Double, String>();
        FileInteraction.readDayHours(dayHours);
        Map<Double, String> nightHours = new HashMap<Double, String>();
        FileInteraction.readNightHours(nightHours);

        for(Graph obj : graphs) {
            obj.startGenerating(NORM_TIME, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);
        }

        FileInteraction.writeWorkTimeInFile(graphs, DAY_OF_MONTH);
        FileInteraction.writeGraphsIntoTemplate(graphs);
        FileInteraction.writeNextCounter(graphs, DAY_OF_MONTH, MONTH, YEAR);
        FileInteraction.deleteOldCounter(MONTH, YEAR);

        System.out.println("Генерация графиков завершена успешно!");
        System.out.println("Версия программы: 1.0.1");
    }
}