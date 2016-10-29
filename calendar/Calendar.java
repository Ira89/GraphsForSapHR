package ru.polynkina.irina.calendar;

public class Calendar {

    private final static int MIN_INDEX_MONTH = 1;
    private final static int MAX_INDEX_MONTH = 12;

    public static int getDayOfMonth(final int MONTH, final int YEAR) {
        try {
            if(MONTH < MIN_INDEX_MONTH || MONTH > MAX_INDEX_MONTH)
                throw new Exception("Месяц может быть в диапазоне от " + MIN_INDEX_MONTH + " до " + MAX_INDEX_MONTH);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(0);
        }

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