package ru.polynkina.irina.unitTests;

import ru.polynkina.irina.period.ReportingPeriod;

import java.util.HashMap;
import java.util.Map;

public class ClassForTestPeriod implements ReportingPeriod {

    private int year;
    private int month;
    private int daysInMonth;
    private double normTime;
    private Map<Integer, Integer> shortAnsHolidays = new HashMap<Integer, Integer>();

    public ClassForTestPeriod(int year, int month, int daysInMonth, double normTime, Map<Integer, Integer> days) {
        this.year = year;
        this.month = month;
        this.daysInMonth = daysInMonth;
        this.normTime = normTime;
        shortAnsHolidays.putAll(days);
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDaysInMonth() { return daysInMonth; }
    public double getNormTime() { return normTime; }
    public Map<Integer, Integer> getCopyShortAndHolidays() {
        return new HashMap<Integer, Integer>(shortAnsHolidays);
    }
}
