package ru.polynkina.irina.period;

import ru.polynkina.irina.graphs.Graph;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserPeriod implements ReportingPeriod {

    private int year;
    private int month;
    private int daysInMonth;
    private double normTime;
    private Map<Integer, Integer> shortAndHolidays = new HashMap<Integer, Integer>();

    public UserPeriod(int year, int month, double normTime, Set<Integer> shortDays, Set<Integer> holidays, Set<Integer> offDays) {
        this.year = year;
        this.month = month;
        daysInMonth = getDaysInMonth(month, year);
        this.normTime = normTime;

        for(Integer day : shortDays) shortAndHolidays.put(day, Graph.CODE_SHORT_DAY);
        for(Integer day : holidays) shortAndHolidays.put(day, Graph.CODE_HOLIDAY);
        for(Integer day : offDays) shortAndHolidays.put(day, Graph.CODE_DAY_OFF);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDaysInMonth() {
        return daysInMonth;
    }

    public double getNormTime() {
        return normTime;
    }

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

    public int calculateDayOfWeek(DayOfWeek dayOfWeek) {
        int counter = 0;
        for(int i = 1; i <= daysInMonth; ++i) {
            if(dayOfWeek.equals(LocalDate.of(year, month, i).getDayOfWeek())) {
                if(shortAndHolidays.containsKey(i)) {
                    for(Map.Entry<Integer, Integer> date : shortAndHolidays.entrySet())
                        if(date.getKey() == i && date.getValue() == 0) ++counter;
                } else {
                    ++counter;
                }
            }
        }
        return counter;
    }

    private int getDaysInMonth(int month, int year) {
        final int[] DAYS_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        final int FEBRUARY_INDEX = 2;
        int daysInMonth = DAYS_IN_MONTH[month];
        if(month == FEBRUARY_INDEX) {
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) ++daysInMonth;
        }
        return daysInMonth;
    }
}