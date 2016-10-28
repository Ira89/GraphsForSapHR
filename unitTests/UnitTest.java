package ru.polynkina.irina.unitTests;

import ru.polynkina.irina.graphs.*;
import java.util.HashMap;
import java.util.Map;

public class UnitTest {

    private Map<Integer, Integer> shortDayAndHolidays = new HashMap<Integer, Integer>();
    private Map<Double, String> dayHours = new HashMap<Double, String>();
    private Map<Double, String> nightHours = new HashMap<Double, String>();
    private static final int DAY_OF_MONTH = 31;
    private static final int INDEX_SHORT_DAY = 0;
    private static final int INDEX_HOLIDAY = 1;
    private static final int INDEX_DAY_OFF = 2;


    public UnitTest() {
        shortDayAndHolidays.put(1, INDEX_SHORT_DAY);
        shortDayAndHolidays.put(2, INDEX_HOLIDAY);
        shortDayAndHolidays.put(3, INDEX_DAY_OFF);

        dayHours.put(0.0, "FREE");
        dayHours.put(1.0, "4AC1");
        dayHours.put(3.0, "4AC3");
        dayHours.put(4.0, "SO20");
        dayHours.put(6.0, "4AC6");
        dayHours.put(6.2, "NO62");
        dayHours.put(7.0, "4AC7");
        dayHours.put(10.0, "4C10");
        dayHours.put(13.0, "4C13");

        nightHours.put(12.0, "4O12");
    }

    public void start() {
        testDayGraphs();
        testDiurnalGraphs();
        testFloatGraphs();
        testMixGraphs();
        testStandardGraphs();
        testShortGraphs();
        testUniqueGraphs();
    }

    private void printInfo(Graph actualGraph, String correctGraph[], double correctNormTime) {
        System.out.print("actual graphs: " + actualGraph.getNormTime() + " -> ");
        for(int i = 0; i < DAY_OF_MONTH; ++i) System.out.print(actualGraph.getWorkTimeSign(i) + " ");
        System.out.print("\ncorrect graph: " + correctNormTime + " -> ");
        for(int i = 0; i < DAY_OF_MONTH; ++i) System.out.print(correctGraph[i] + " ");
    }

    private boolean isEquals(Graph actualGraph, String correctGraph[], double correctNormTime) {
        for(int i = 0; i < DAY_OF_MONTH; ++i)
            if (!correctGraph[i].equals(actualGraph.getWorkTimeSign(i))) return false;
        return correctNormTime - actualGraph.getNormTime() < 0.001;
    }

    // FL12
    private void testDayGraphs() {
        double correctNormTime = 167;
        String correctGraph[] = {
                "FL11", "FL11", "4C10", "FREE", "FREE", "FREE", "4C10",
                "FL11", "4C10", "FREE", "FREE", "FREE", "FL11", "4C10",
                "FL11", "FREE", "FREE", "FREE", "4C10", "4C10", "FL11",
                "FREE", "FREE", "FREE", "4C10", "FL11", "4C10", "FREE",
                "FREE", "FREE", "FL11"
        };

        Graph actualGraph = new Graph(1, "DAY", "dddfff", 11, "FL11");
        actualGraph.setCounter(0);
        actualGraph.startGenerating(167, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph, correctNormTime)) {
            System.out.println("DAY GRAPHS: ERROR");
            printInfo(actualGraph, correctGraph, correctNormTime);
        } else {
            System.out.println("DAY GRAPHS: OK");
        }
    }

    // SUT1
    private void testDiurnalGraphs() {
        double correctNormTime = 167;
        String correctGraph[] = {
                "FREE", "FREE", "FREE", "SUTK", "FREE", "4C13", "FREE",
                "SUTK", "FREE", "FREE", "FREE", "SUTK", "FREE", "FREE",
                "FREE", "SUTK", "FREE", "FREE", "FREE", "SUTK", "FREE",
                "FREE", "FREE", "SUTK", "FREE", "FREE", "FREE", "SUTK",
                "FREE", "FREE", "FREE"
        };

        Graph actualGraph = new GraphDiurnal(1, "DIURNAL", "nfff", 22, "SUTK");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(167, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph, correctNormTime)) {
            System.out.println("DIURNAL GRAPHS: ERROR");
            printInfo(actualGraph, correctGraph, correctNormTime);
        } else {
            System.out.println("DIURNAL GRAPHS: OK");
        }
    }

    // G5-2
    private void testFloatGraphs() {
        double correctNormTime = 150.2;
        String correctGraph[] = {
                "NO72", "NO72", "4AC7", "4AC7", "4AC6", "FREE", "FREE",
                "4AC7", "4AC6", "NO62", "4AC7", "4AC7", "FREE", "FREE",
                "4AC6", "4AC7", "4AC6", "NO62", "4AC7", "FREE", "FREE",
                "4AC7", "4AC6", "4AC7", "4AC6", "NO62", "FREE", "FREE",
                "4AC7", "4AC6", "NO62"
        };

        Graph actualGraph = new GraphFloat(1, "FLOAT", "dddddff", 7.2, "NO72");
        actualGraph.setCounter(0);
        actualGraph.startGenerating(167, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph, correctNormTime)) {
            System.out.println("FLOAT GRAPHS: ERROR");
            printInfo(actualGraph, correctGraph, correctNormTime);
        } else {
            System.out.println("FLOAT GRAPHS: OK");
        }
    }

    // LOG1
    private void testMixGraphs() {
        int correctNormTime = 167;
        String correctGraph[] = {
                "CNO4", "C_33", "FREE", "FREE", "FREE", "FREE", "CDEN",
                "CDEN", "CNO4", "4O12", "FREE", "FREE", "FREE", "FREE",
                "CDEN", "CDEN", "CNO4", "4O12", "FREE", "FREE", "FREE",
                "FREE", "CDEN", "CDEN", "CNO4", "4O12", "FREE", "FREE",
                "FREE", "FREE", "CDEN"
        };

        Graph actualGraph = new GraphMix(1, "MIX", "ddnnffff", 11, "CDEN", 11, "CNO4");
        actualGraph.setCounter(2);
        actualGraph.startGenerating(167, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph, correctNormTime)) {
            System.out.println("MIX GRAPHS: ERROR");
            printInfo(actualGraph, correctGraph, correctNormTime);
        } else {
            System.out.println("MIX GRAPHS: OK");
        }
    }

    // PAR6
    private void testShortGraphs() {
        int correctNormTime = 83;
        String correctGraph[] = {
                "SO20", "SO20", "SO20", "FREE", "FREE", "SO20", "4AC3",
                "SO20", "SO20", "4AC3", "FREE", "FREE", "SO20", "4AC3",
                "SO20", "SO20", "4AC3", "FREE", "FREE", "SO20", "SO20",
                "4AC3", "SO20", "4AC3", "FREE", "FREE", "SO20", "SO20",
                "4AC3", "SO20", "4AC3"
        };

        Graph actualGraph = new GraphShort(1, "SHORT", "dddddff", 4, "SO20");
        actualGraph.setCounter(2);
        actualGraph.startGenerating(167, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph, correctNormTime)) {
            System.out.println("SHORT GRAPHS: ERROR");
            printInfo(actualGraph, correctGraph, correctNormTime);
        } else {
            System.out.println("SHORT GRAPHS: OK");
        }
    }

    // NEP4
    private void testStandardGraphs() {
        int correctNormTime = 146;
        String correctGraph[] = {
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4"
        };

        Graph actualGraph = new GraphStandard(1, "STANDARD", "dddddff", 7, "NEP4");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(167, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph, correctNormTime)) {
            System.out.println("STANDARD GRAPHS: ERROR");
            printInfo(actualGraph, correctGraph, correctNormTime);
        } else {
            System.out.println("STANDARD GRAPHS: OK");
        }
    }

    // NEP6
    private void testUniqueGraphs() {
        int correctNormTime = 116;
        String correctGraph[] = {
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5"
        };

        Graph actualGraph = new GraphUnique(1, "UNIQUE", "dududff", 6, "NEP3", 5, "NEP5");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(167, DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph, correctNormTime)) {
            System.out.println("UNIQUE GRAPHS: ERROR");
            printInfo(actualGraph, correctGraph, correctNormTime);
        } else {
            System.out.println("UNIQUE GRAPHS: OK");
        }
    }
}