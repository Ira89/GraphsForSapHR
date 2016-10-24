package ru.polynkina.irina.calendar;

public class Calendar {

    public static int getAmountDay(final int MONTH, final int YEAR){
        final int[] DAY_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        final int INDEX_MONTH_EXCLUSION = 2;

        if(MONTH != INDEX_MONTH_EXCLUSION) return DAY_IN_MONTH[MONTH];

        boolean isDividedBy400 = YEAR % 400 == 0;
        if(isDividedBy400) return DAY_IN_MONTH[MONTH] + 1;

        boolean isDividedBy4 = YEAR % 4 == 0;
        boolean isDividedBy100 = YEAR % 100 == 0;
        if(isDividedBy4 && !isDividedBy100) return DAY_IN_MONTH[MONTH] + 1;
        else return DAY_IN_MONTH[MONTH];
    }
}