package ru.polynkina.irina.unitTests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.polynkina.irina.period.ReportingPeriod;
import ru.polynkina.irina.period.UserPeriod;

public class TestPeriod {

    private static ReportingPeriod period;
    private static final int MAX_INDEX_MONTH = 12;

    @BeforeClass
    public static void ini() throws Exception {
        period = new UserPeriod();
    }

    @Test
    public void getNextMonth() {
        int currMonth = period.getMonth();
        int nextMonth = currMonth + 1 > MAX_INDEX_MONTH ? 1 : currMonth + 1;
        Assert.assertEquals(nextMonth, period.getNextMonth());
    }

    @Test
    public void getYearAfterIncreaseMonth() {
        int currYear = period.getYear();
        int nextYear = period.getMonth() + 1 > MAX_INDEX_MONTH ? ++currYear : currYear;
        Assert.assertEquals(nextYear, period.getYearAfterIncreaseMonth());
    }
}
