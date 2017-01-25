package ru.polynkina.irina.unitTests;

import ru.polynkina.irina.graphs.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.fail;

public class UnitTest {

    private static Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();
    private static Map<Double, String> dayHours = new HashMap<Double, String>();
    private static Map<Double, String> nightHours = new HashMap<Double, String>();
    private static final int DAYS_IN_MONTH = 31;
    private static final int INDEX_SHORT_DAY = 0;
    private static final int INDEX_HOLIDAY = 1;
    private static final int INDEX_DAY_OFF = 2;

    @BeforeClass
    public static void init() {
        shortAndHolidays.put(1, INDEX_SHORT_DAY);
        shortAndHolidays.put(2, INDEX_HOLIDAY);
        shortAndHolidays.put(3, INDEX_DAY_OFF);

        dayHours.put(0.0, "FREE");
        dayHours.put(1.0, "4AC1");
        dayHours.put(3.0, "4AC3");
        dayHours.put(4.0, "SO20");
        dayHours.put(5.0, "4AC5");
        dayHours.put(5.2, "NO52");
        dayHours.put(6.0, "4AC6");
        dayHours.put(6.2, "NO62");
        dayHours.put(7.0, "4AC7");
        dayHours.put(10.0, "4C10");
        dayHours.put(13.0, "4C13");

        nightHours.put(12.0, "4O12");
    }

    private String makeDebugInfo(DayGraph actualGraph, String correctGraph[], double correctNormTime) throws Exception {
        String textInfo = '\n' + " actual -> " + actualGraph.getNormTime() + " ";
        for(int i = 0; i < DAYS_IN_MONTH; ++i) textInfo += actualGraph.getWorkTimeSign(i) + " ";

        textInfo += '\n' + "correct -> " + correctNormTime + " ";
        for(int i = 0; i < DAYS_IN_MONTH; ++i) textInfo += correctGraph[i] + " ";
        return textInfo;
    }

    private boolean graphsIsEquals(DayGraph actualGraph, String correctGraph[], double correctNormTime) throws Exception {
        for(int i = 0; i < DAYS_IN_MONTH; ++i) {
            if (!correctGraph[i].equals(actualGraph.getWorkTimeSign(i))) return false;
        }
        return correctNormTime - actualGraph.getNormTime() < 0.001;
    }

    @Test
    public void testDayGraph() throws Exception {
        final double correctNormTime = 167;
        final String correctGraph[] = {
                "FL11", "FL11", "4C10", "FREE", "FREE", "FREE", "4C10",
                "FL11", "4C10", "FREE", "FREE", "FREE", "FL11", "4C10",
                "FL11", "FREE", "FREE", "FREE", "4C10", "4C10", "FL11",
                "FREE", "FREE", "FREE", "4C10", "FL11", "4C10", "FREE",
                "FREE", "FREE", "FL11"
        };

        DayGraph actualGraph = new DayGraph(1, "DAY", "dddfff", 11, "FL11", "text");
        actualGraph.setCounter(0);
        actualGraph.startGenerating(167, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("DAY (FL12)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testDiurnalGraph() throws Exception {
        final double correctNormTime = 167;
        final String correctGraph[] = {
                "FREE", "FREE", "FREE", "SUTK", "FREE", "4C13", "FREE",
                "SUTK", "FREE", "FREE", "FREE", "SUTK", "FREE", "FREE",
                "FREE", "SUTK", "FREE", "FREE", "FREE", "SUTK", "FREE",
                "FREE", "FREE", "SUTK", "FREE", "FREE", "FREE", "SUTK",
                "FREE", "FREE", "FREE"
        };

        DayGraph actualGraph = new DiurnalGraph(1, "DIURNAL", "nfff", 22, "SUTK", "text");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(167, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("DIURNAL (SUT1)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testFractionalGraph() throws Exception {
        final double correctNormTime = 150.2;
        final String correctGraph[] = {
                "NO72", "NO72", "4AC7", "4AC7", "4AC6", "FREE", "FREE",
                "4AC7", "4AC6", "NO62", "4AC7", "4AC7", "FREE", "FREE",
                "4AC6", "4AC7", "NO62", "4AC6", "4AC7", "FREE", "FREE",
                "4AC7", "4AC6", "NO62", "4AC7", "4AC6",  "FREE", "FREE",
                "4AC7", "4AC6", "NO62"
        };

        DayGraph actualGraph = new FractionalGraph(1, "FLOAT", "dddddff", 7.2, "NO72", "text");
        actualGraph.setCounter(0);
        actualGraph.startGenerating(167, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("FLOAT (G5-2)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testMixedGraph() throws Exception {
        final double correctNormTime = 167;
        final String correctGraph[] = {
                "CNO4", "C_33", "FREE", "FREE", "FREE", "FREE", "CDEN",
                "CDEN", "CNO4", "4O12", "FREE", "FREE", "FREE", "FREE",
                "CDEN", "CDEN", "CNO4", "4O12", "FREE", "FREE", "FREE",
                "FREE", "CDEN", "CDEN", "CNO4", "4O12", "FREE", "FREE",
                "FREE", "FREE", "CDEN"
        };

        DayGraph actualGraph = new MixedGraph(1, "MIX", "ddnnffff", 11, "CDEN", "text", 11, "CNO4");
        actualGraph.setCounter(2);
        actualGraph.startGenerating(167, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("MIX (LOG1)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testShortGraph() throws Exception {
        final double correctNormTime = 83;
        final String correctGraph[] = {
                "SO20", "SO20", "SO20", "FREE", "FREE", "SO20", "4AC3",
                "SO20", "SO20", "4AC3", "FREE", "FREE", "SO20", "4AC3",
                "SO20", "SO20", "4AC3", "FREE", "FREE", "SO20", "SO20",
                "4AC3", "SO20", "4AC3", "FREE", "FREE", "SO20", "SO20",
                "4AC3", "SO20", "4AC3"
        };

        DayGraph actualGraph = new ShortGraph(1, "SHORT", "dddddff", 4, "SO20", "text");
        actualGraph.setCounter(2);
        actualGraph.startGenerating(167, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("SHORT (PAR6)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testFiveDayGraph() throws Exception {
        final double correctNormTime = 146;
        final String correctGraph[] = {
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4", "NEP4", "FREE", "FREE", "NEP4",
                "NEP4", "NEP4", "NEP4"
        };

        DayGraph actualGraph = new FiveDayGraph(1, "STANDARD", "dddddff", 7, "NEP4", "text");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(167, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("STANDARD (NEP4)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testUniqueGraph() throws Exception {
        final double correctNormTime = 116;
        final String correctGraph[] = {
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5", "NEP3", "FREE", "FREE", "NEP3",
                "NEP5", "NEP3", "NEP5"
        };

        DayGraph actualGraph = new UniqueGraph(1, "UNIQUE", "dududff", 6, "NEP3", "text", 5, "NEP5");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(167, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("UNIQUE (NEP6)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testRoundingHours() throws Exception {
        final double correctNormTime = 128.6;
        final String correctGraph[] = {
                "NO72", "NO72", "FREE", "FREE", "4AC6", "4AC6", "4AC6",
                "4AC6", "4AC5", "FREE", "FREE", "4AC6", "4AC6", "4AC6",
                "4AC6", "4AC5", "FREE", "FREE", "4AC6", "4AC6", "4AC6",
                "4AC6", "4AC5", "FREE", "FREE", "4AC6", "4AC6", "4AC6",
                "4AC5", "NO52", "FREE"
        };

        DayGraph actualGraph = new FractionalGraph(1, "FLOAT", "dddddff", 7.2, "NO72", "text");
        actualGraph.setCounter(3);
        actualGraph.startGenerating(143, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("FLOAT (G5/2)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testSetFractionalDay() throws Exception {
        Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();
        shortAndHolidays.put(7, 0);
        shortAndHolidays.put(8, 1);
        shortAndHolidays.put(27, 0);
        shortAndHolidays.put(28, 1);

        final double correctNormTime = 149.2;
        final String correctGraph[] = {
                "4AC6", "4AC6", "4AC7", "4AC6", "FREE", "FREE", "NO72",
                "NO72", "4AC7", "4AC6", "NO62", "FREE", "FREE", "4AC7",
                "4AC6", "4AC7", "4AC6", "4AC7", "FREE", "FREE", "4AC6",
                "NO62", "4AC7", "4AC6", "4AC7", "FREE", "FREE", "NO72",
                "4AC6", "4AC7", "NO62"
        };

        DayGraph actualGraph = new FractionalGraph(1, "FLOAT", "dddddff", 7.2, "NO72", "text");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(166, DAYS_IN_MONTH, shortAndHolidays, dayHours, nightHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("FLOAT (G5-2)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }
}