package ru.polynkina.irina.period;

import java.util.Map;

public interface ReportingPeriod {

    int getYear();
    int getMonth();
    int getDaysInMonth();
    double getNormTime();
    Map<Integer, Integer> getCopyShortAndHolidays();

    int getNextMonth();
    int getYearAfterIncreaseMonth();

}
