package ru.polynkina.irina.unitTests;

import ru.polynkina.irina.graphs.*;
import ru.polynkina.irina.hours.*;
import java.util.HashSet;
import java.util.Set;

import org.junit.*;
import ru.polynkina.irina.period.UserPeriod;

import static org.junit.Assert.fail;

public class TestGraphs {

    private static Set<Integer> shortDays = new HashSet<>();
    private static Set<Integer> holidays = new HashSet<>();
    private static Set<Integer> offDays = new HashSet<>();
    private static UserPeriod period;
    private static LibHours libHours;

    @BeforeClass
    public static void init() throws Exception {
        shortDays.add(1);
        holidays.add(2);
        offDays.add(3);

        period = new UserPeriod(2017, 1, 167, shortDays, holidays, offDays);
        libHours = new LibHours();
    }

    private String makeDebugInfo(DayGraph actualGraph, String correctGraph[], double correctNormTime) throws Exception {
        String textInfo = '\n' + " actual -> " + actualGraph.getNormTime() + " ";
        for(int i = 0; i < period.getDaysInMonth(); ++i) textInfo += actualGraph.getWorkTimeSign(i) + " ";

        textInfo += '\n' + "correct -> " + correctNormTime + " ";
        for(int i = 0; i < period.getDaysInMonth(); ++i) textInfo += correctGraph[i] + " ";
        return textInfo;
    }

    private boolean graphsIsEquals(DayGraph actualGraph, String correctGraph[], double correctNormTime) {
        for(int i = 0; i < period.getDaysInMonth(); ++i) {
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
        actualGraph.startGenerating(period, libHours);

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
        actualGraph.startGenerating(period, libHours);

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
        actualGraph.startGenerating(period, libHours);

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
        actualGraph.startGenerating(period, libHours);

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
        actualGraph.startGenerating(period, libHours);

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
        actualGraph.startGenerating(period, libHours);

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
        actualGraph.startGenerating(period, libHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("UNIQUE (NEP6)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testSmallGraph() throws Exception {
        final double correctNormTime = 17;
        final String correctGraph[] = {
                "4AC1", "4AC1", "4AC1", "FREE", "FREE", "FREE", "4AC1",
                "4AC1", "4AC1", "4AC1", "FREE", "FREE", "FREE", "4AC1",
                "4AC1", "4AC1", "4AC1", "FREE", "FREE", "FREE", "4AC1",
                "4AC1", "4AC1", "4AC1", "FREE", "FREE", "FREE", "4AC1",
                "4AC1", "4AC1", "4AC1"
        };

        Set<Integer> shortDays = new HashSet<>();
        shortDays.add(29);

        Set<Integer> holidays = new HashSet<>();
        holidays.add(30);

        Set<Integer> offDays = new HashSet<>();
        offDays.add(31);

        UserPeriod period = new UserPeriod(2017, 1, 166, shortDays, holidays, offDays);

        DayGraph actualGraph = new SmallGraph(1, "SMALL", "ddddfff", 1, "4AC1", "text");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(period, libHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("SMALL (SO01) " + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public  void testPersonalGraph() throws Exception {
        final double correctNormTime = 148;
        final String correctGraph[] = {
                "NORM", "4AC7", "FREE", "FREE", "4AC7", "4AC6", "4AC7",
                "NORM", "4AC7", "FREE", "FREE", "4AC6", "4AC7", "4AC7",
                "4AC6", "4AC7", "FREE", "FREE", "4AC7", "4AC6", "4AC7",
                "4AC7", "4AC6", "FREE", "FREE", "4AC7", "4AC7", "4AC6",
                "4AC7", "4AC6", "FREE"
        };

        Set<Integer> shortDays = new HashSet<>();
        shortDays.add(1);

        Set<Integer> holidays = new HashSet<>();
        holidays.add(8);

        Set<Integer> offDays = new HashSet<>();
        offDays.add(15);

        UserPeriod period = new UserPeriod(2017, 12, 151, shortDays, holidays, offDays);

        DayGraph actualGraph = new PersonalGraph(1, "PERSONAL", "dddddff", 8, "NORM", "text");
        actualGraph.setCounter(3);
        actualGraph.startGenerating(period, libHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("PERSONAL (DIR2) " + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
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

        UserPeriod period = new UserPeriod(2017, 1, 143, shortDays, holidays, offDays);
        DayGraph actualGraph = new FractionalGraph(1, "FLOAT", "dddddff", 7.2, "NO72", "text");
        actualGraph.setCounter(3);
        actualGraph.startGenerating(period, libHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("FLOAT (G5/2)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }

    @Test
    public void testSetFractionalDay() throws Exception {
        final double correctNormTime = 149.2;
        final String correctGraph[] = {
                "4AC6", "4AC6", "4AC7", "4AC6", "FREE", "FREE", "NO72",
                "NO72", "4AC7", "4AC6", "NO62", "FREE", "FREE", "4AC7",
                "4AC6", "4AC7", "4AC6", "4AC7", "FREE", "FREE", "4AC6",
                "NO62", "4AC7", "4AC6", "4AC7", "FREE", "FREE", "NO72",
                "4AC6", "4AC7", "NO62"
        };

        Set<Integer> shortDays = new HashSet<>();
        shortDays.add(7);
        shortDays.add(27);

        Set<Integer> holidays = new HashSet<>();
        holidays.add(8);
        holidays.add(28);

        Set<Integer> offDays = new HashSet<>();

        UserPeriod period = new UserPeriod(2017, 1, 166, shortDays, holidays, offDays);
        DayGraph actualGraph = new FractionalGraph(1, "FLOAT", "dddddff", 7.2, "NO72", "text");
        actualGraph.setCounter(1);
        actualGraph.startGenerating(period, libHours);

        if(!graphsIsEquals(actualGraph, correctGraph, correctNormTime))
            fail("FLOAT (G5-2)" + makeDebugInfo(actualGraph, correctGraph, correctNormTime));
    }
}