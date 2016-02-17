package ru.polynkina.irina.graphs;

public class Calendar {

    public static final int[] DAY_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final int INDEX_MONTH_EXCLUSION = 2;

    public static int getAmountDay(final int INDEX_MONTH, final int INDEX_YEAR){
        if(INDEX_MONTH != INDEX_MONTH_EXCLUSION) return DAY_IN_MONTH[INDEX_MONTH];

        boolean isDividedBy400 = INDEX_YEAR % 400 == 0;
        if(isDividedBy400) return DAY_IN_MONTH[INDEX_MONTH] + 1;

        boolean isDividedBy4 = INDEX_YEAR % 4 == 0;
        boolean isDividedBy100 = INDEX_YEAR % 100 == 0;
        if(isDividedBy4 && !isDividedBy100) return DAY_IN_MONTH[INDEX_MONTH] + 1;
        else return DAY_IN_MONTH[INDEX_MONTH];
    }
}