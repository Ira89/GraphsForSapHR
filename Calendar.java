package ru.polynkina.irina.graphs;

public class Calendar {

    public static final int[] DAY_IN_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final int INDEX_MONTH_EXCLUSION = 2;

    public static int getAmountDay(int indexMonth, int indexYear){
        if(indexMonth != INDEX_MONTH_EXCLUSION) return DAY_IN_MONTH[indexMonth];

        boolean isDividedBy400 = indexYear % 400 == 0;
        if(isDividedBy400) return DAY_IN_MONTH[indexMonth] + 1;

        boolean isDividedBy4 = indexYear % 4 == 0;
        boolean isDividedBy100 = indexYear % 100 == 0;
        if(isDividedBy4 && !isDividedBy100) return DAY_IN_MONTH[indexMonth] + 1;
        else return DAY_IN_MONTH[indexMonth];
    }
}