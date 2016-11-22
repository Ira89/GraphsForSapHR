package ru.polynkina.irina.calendar;

public class Calendar {

    private final static int MIN_INDEX_MONTH = 1;
    private final static int MAX_INDEX_MONTH = 12;
    private final static int FEBRUARY_INDEX = 2;
    private final static int[] DAYS_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static int getDaysInMonth(int month, int year) throws Exception {
        if(month < MIN_INDEX_MONTH || month > MAX_INDEX_MONTH)
            throw new Exception("Месяц может быть в диапазоне от " + MIN_INDEX_MONTH + " до " + MAX_INDEX_MONTH);

        int daysInMonth = DAYS_IN_MONTH[month];
        if(month == FEBRUARY_INDEX) {
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) ++daysInMonth;
        }
        return daysInMonth;
    }
}