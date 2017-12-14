package ru.polynkina.irina.unitTests;

import ru.polynkina.irina.period.ReportingPeriod;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ClassForTestPeriod implements ReportingPeriod  {

    private int year;
    private int month;
    private int daysInMonth;
    private double normTime;
    private Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();

    public ClassForTestPeriod(int year, int month, int daysInMonth, double normTime, Map<Integer, Integer> days) {
        this.year = year;
        this.month = month;
        this.daysInMonth = daysInMonth;
        this.normTime = normTime;
        shortAndHolidays.putAll(days);
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDaysInMonth() { return daysInMonth; }
    public double getNormTime() { return normTime; }

    public Map<Integer, Integer> getCopyShortAndHolidays() {
        Map<Integer, Integer> copyShortAndHolidays = new HashMap<Integer, Integer>();
        for(Map.Entry<Integer, Integer> copy : shortAndHolidays.entrySet()) {
            Integer indexDay = copy.getKey() - 1;
            Integer codeDay = copy.getValue();
            copyShortAndHolidays.put(indexDay, codeDay);
        }
        return copyShortAndHolidays;
    }

    public int getNextMonth() {
        final int MAX_INDEX_MONTH = 12;
        int nextMonth = month + 1;
        if(nextMonth > MAX_INDEX_MONTH) nextMonth = 1;
        return nextMonth;
    }

    public int getYearAfterIncreaseMonth() {
        final int MAX_INDEX_MONTH = 12;
        int nextYear = year;
        if (month + 1 > MAX_INDEX_MONTH) ++nextYear;
        return nextYear;
    }

    @Override
    public int calculateDayOfWeek(DayOfWeek dayOfWeek) {
        int counter = 0;
        for(int i = 1; i < daysInMonth; ++i) {
            if(dayOfWeek.equals(LocalDate.of(year, month, i).getDayOfWeek())) {
                ++counter;
            }
        }
        return counter;
    }
}
