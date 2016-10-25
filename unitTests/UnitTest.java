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
        dayHours.put(7.0, "4AC7");
        dayHours.put(10.0, "4C10");
        dayHours.put(15.0, "4C15");

        nightHours.put(12.0, "4O12");
    }

    private void printActualGraph(Graph graph) {
        System.out.print("actual graphs: ");
        for(int i = 0; i < DAY_OF_MONTH; ++i) System.out.print(graph.getWorkTimeSign(i) + " ");
        System.out.println();
    }

    private void printCorrectGraph(String graph[]) {
        System.out.print("correct graph: ");
        for(int i = 0; i < DAY_OF_MONTH; ++i) System.out.print(graph[i] + " ");
        System.out.println();
    }

    private boolean isEquals(Graph actualGraph, String correctGraph[]) {
        for(int i = 0; i < DAY_OF_MONTH; ++i) {
            if (!correctGraph[i].equals(actualGraph.getWorkTimeSign(i))) return false;
        }
        return true;
    }

    private void testGraphs() {
        String correctGraph[] = {
                "FL11", "FL11", "FL11", "FREE", "FREE", "FREE", "FL11",
                "4C10", "FL11", "FREE", "FREE", "FREE", "4C10", "FL11",
                "4C10", "FREE", "FREE", "FREE", "FL11", "FL11", "4C10",
                "FREE", "FREE", "FREE", "FL11", "4C10", "FL11", "FREE",
                "FREE", "FREE", "4C10"
        };

        Graph actualGraph = new Graph(1, "graph", "dddfff", 11, "FL11", 169);
        actualGraph.setCounter(0);
        actualGraph.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph)) {
            System.out.println("GRAPHS: ERROR");
            printCorrectGraph(correctGraph);
            printActualGraph(actualGraph);
        } else {
            System.out.println("GRAPHS: OK");
        }
    }

    private void testDiurnalGraphs() {
        String correctGraph[] = {
                "FREE", "FREE", "FREE", "SUTK", "FREE", "4C15", "FREE",
                "SUTK", "FREE", "4AC1", "FREE", "SUTK", "FREE", "FREE",
                "FREE", "SUTK", "FREE", "FREE", "FREE", "SUTK", "FREE",
                "FREE", "FREE", "SUTK", "FREE", "FREE", "FREE", "SUTK",
                "FREE", "FREE", "FREE"
        };

        Graph actualGraph = new GraphDiurnal(1, "diurnal", "nfff", 22, "SUTK", 170);
        actualGraph.setCounter(1);
        actualGraph.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph)) {
            System.out.println("DIURNAL GRAPHS: ERROR");
            printCorrectGraph(correctGraph);
            printActualGraph(actualGraph);
        } else {
            System.out.println("DIURNAL GRAPHS: OK");
        }
    }

    private void testFloatGraphs() {
        String correctGraph[] = {
                "NO72", "NO72", "4AC7", "4AC7", "4AC6", "FREE", "FREE",
                "4AC7", "4AC6", "4AC7", "4AC6", "4AC7", "FREE", "FREE",
                "4AC7", "4AC6", "4AC7", "4AC6", "4AC7", "FREE", "FREE",
                "4AC6", "4AC7", "4AC7", "4AC6", "4AC7", "FREE", "FREE",
                "4AC6", "4AC7", "4AC6"
        };

        Graph actualGraph = new GraphFloat(1, "float", "dddddff", 7.2, "NO72", 169);
        actualGraph.setCounter(0);
        actualGraph.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph)) {
            System.out.println("FLOAT GRAPHS: ERROR");
            printCorrectGraph(correctGraph);
            printActualGraph(actualGraph);
        } else {
            System.out.println("FLOAT GRAPHS: OK");
        }
    }

    private void testMixGraphs() {
        String correctGraph[] = {
                "CNO4", "C_33", "FREE", "FREE", "FREE", "FREE", "CDEN",
                "CDEN", "4O12", "4O12", "FREE", "FREE", "FREE", "FREE",
                "CDEN", "CDEN", "4O12", "4O12", "FREE", "FREE", "FREE",
                "FREE", "CDEN", "CDEN", "4O12", "CNO4", "FREE", "FREE",
                "FREE", "FREE", "CDEN"
        };

        Graph actualGraph = new GraphMix(1, "mix", "ddnnffff", 11, "CDEN", 11, "CNO4", 169);
        actualGraph.setCounter(2);
        actualGraph.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph)) {
            System.out.println("MIX GRAPHS: ERROR");
            printCorrectGraph(correctGraph);
            printActualGraph(actualGraph);
        } else {
            System.out.println("MIX GRAPHS: OK");
        }
    }

    private void testShortGraphs() {
        String correctGraph[] = {
                "SO20", "SO20", "SO20", "FREE", "FREE", "SO20", "4AC3",
                "SO20", "SO20", "4AC3", "FREE", "FREE", "SO20", "SO20",
                "4AC3", "SO20", "SO20", "FREE", "FREE", "4AC3", "SO20",
                "SO20", "4AC3", "SO20", "FREE", "FREE", "SO20", "4AC3",
                "SO20", "SO20", "4AC3"
        };

        Graph actualGraph = new GraphShort(1, "short", "dddddff", 4, "SO20", 169);
        actualGraph.setCounter(2);
        actualGraph.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph)) {
            System.out.println("SHORT GRAPHS: ERROR");
            printCorrectGraph(correctGraph);
            printActualGraph(actualGraph);
        } else {
            System.out.println("SHORT GRAPHS: OK");
        }
    }

    private void testStandardGraphs() {
        String correctGraph[] = {
                "NORM", "NORM", "NORM", "NORM", "FREE", "FREE", "NORM",
                "NORM", "NORM", "NORM", "NORM", "FREE", "FREE", "NORM",
                "NORM", "NORM", "NORM", "NORM", "FREE", "FREE", "NORM",
                "NORM", "NORM", "NORM", "NORM", "FREE", "FREE", "NORM",
                "NORM", "NORM", "NORM"
        };

        Graph actualGraph = new GraphStandard(1, "standard", "dddddff", 8, "NORM", 169);
        actualGraph.setCounter(1);
        actualGraph.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph)) {
            System.out.println("STANDARD GRAPHS: ERROR");
            printCorrectGraph(correctGraph);
            printActualGraph(actualGraph);
        } else {
            System.out.println("STANDARD GRAPHS: OK");
        }
    }

    private void testUniqueGraphs() {
        String correctGraph[] = {
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5"
        };

        Graph actualGraph = new GraphUnique(1, "unique", "dududff", 6, "NEP3", 5, "NEP5", 169);
        actualGraph.setCounter(1);
        actualGraph.startGenerating(DAY_OF_MONTH, shortDayAndHolidays, dayHours, nightHours);

        if(!isEquals(actualGraph, correctGraph)) {
            System.out.println("UNIQUE GRAPHS: ERROR");
            printCorrectGraph(correctGraph);
            printActualGraph(actualGraph);
        } else {
            System.out.println("UNIQUE GRAPHS: OK");
        }
    }

    public void start() {
        testGraphs();
        testDiurnalGraphs();
        testFloatGraphs();
        testMixGraphs();
        testStandardGraphs();
        testShortGraphs();
        testUniqueGraphs();
    }
}
