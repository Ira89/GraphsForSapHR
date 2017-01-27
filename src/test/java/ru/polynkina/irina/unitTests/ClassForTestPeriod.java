package ru.polynkina.irina.unitTests;

import ru.polynkina.irina.period.ReportingPeriod;

public class ClassForTestPeriod implements ReportingPeriod {

    private int year;
    private int month;
    private int daysInMonth;
    private double normTime;

    public ClassForTestPeriod(int year, int month, int daysInMonth, double normTime) {
        this.year = year;
        this.month = month;
        this.daysInMonth = daysInMonth;
        this.normTime = normTime;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDaysInMonth() { return daysInMonth; }
    public double getNormTime() { return normTime; }
}
