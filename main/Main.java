package ru.polynkina.irina.main;

import ru.polynkina.irina.fileInteraction.FileInteraction;
import ru.polynkina.irina.graphs.DayGraph;
import ru.polynkina.irina.userInteraction.UserForm;
import ru.polynkina.irina.unitTests.UnitTest;
import ru.polynkina.irina.calendar.Calendar;
import java.util.*;

public class Main {

    public static void main(String[] needRunningTests) {
        if(needRunningTests != null && needRunningTests[0].equals("1")) {
            UnitTest test = new UnitTest();
            test.start();
        }

        final int YEAR = UserForm.readYear();
        final int MONTH = UserForm.readMonth();
        final int DAY_OF_MONTH = Calendar.getDayOfMonth(MONTH, YEAR);
        final double NORM_TIME = UserForm.readNormTime();

        Map<Integer, Integer> shortDayAndHolidays = new LinkedHashMap<Integer, Integer>();
        UserForm.readShortDaysAndHolidays(shortDayAndHolidays, DAY_OF_MONTH);

        Map<Double, String> dayHours = new HashMap<Double, String>();
        FileInteraction.readDayHours(dayHours);
        Map<Double, String> nightHours = new HashMap<Double, String>();
        FileInteraction.readNightHours(nightHours);

        List<DayGraph> graphs = new ArrayList<DayGraph>();
        FileInteraction.fabricateGraphs(graphs);
        FileInteraction.readCountersForGraphs(graphs, MONTH, YEAR);

        for(DayGraph obj : graphs)
            obj.startGenerating(NORM_TIME, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);
        FileInteraction.writeWorkTimeInFile(graphs, DAY_OF_MONTH);


        Map<String, Integer> regions = new HashMap<String, Integer>();
        UserForm.readRegions(regions);

        for(Map.Entry<String, Integer> obj : regions.entrySet()) {
            if(obj.getValue() == 0) continue;
            List<String> graphsForRegion = new ArrayList<String>();
            FileInteraction.readGraphsForRegion(obj.getKey(), graphsForRegion);
            FileInteraction.writeGraphsIntoTemplate(graphs, graphsForRegion, obj.getKey(), MONTH, YEAR);
        }

        FileInteraction.writeNextCounter(graphs, DAY_OF_MONTH, MONTH, YEAR);
        FileInteraction.deleteOldCounter(MONTH, YEAR);

        System.out.println("Генерация графиков завершена успешно!");
        System.out.println("Версия программы: 2.0.0");
    }
}