package ru.polynkina.irina.unitTests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.polynkina.irina.period.ReportingPeriod;
import ru.polynkina.irina.period.UserPeriod;

import java.util.HashSet;
import java.util.Set;

public class TestPeriod {

    private static ReportingPeriod period;
    private static Set<Integer> shortDays = new HashSet<Integer>(1);
    private static Set<Integer> holidays = new HashSet<Integer>(2);
    private static Set<Integer> offDays = new HashSet<Integer>(3);

    @BeforeClass
    public static void ini() throws Exception {
        period = new UserPeriod(2017, 12, 184, shortDays, holidays, offDays);
    }

    @Test
    public void testGetNextMonth() {
        Assert.assertTrue(period.getNextMonth() == 1);
    }

    @Test
    public void testGetYearAfterIncreaseMonth() {
        Assert.assertTrue(period.getYearAfterIncreaseMonth() == 2018);
    }
}
