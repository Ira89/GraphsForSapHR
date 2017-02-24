package ru.polynkina.irina.graphs;

public class FractionalGraph extends DayGraph {

    public FractionalGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    // Смены с минутами тоже ставим с учетом частоты, чтобы они были равномерно распределены в рамках целого месяца
    private void setFloatDay(double floatTime, int amountFloatDays, int amountMissingDays, int daysInMonth) throws Exception {
        int amountIntegerDays = amountMissingDays - amountFloatDays;
        double frequency = calcFrequency(amountFloatDays, amountIntegerDays);

        double currentFrequency = 0;
        int counterFloatDays = 0;
        int counterIntegerDays = 0;

        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            if(getWorkTime(indexDay) != UNINITIALIZED_WORK_TIME) continue;
            if(currentFrequency < frequency && counterIntegerDays < amountIntegerDays || counterFloatDays == amountFloatDays) {
                ++counterIntegerDays;
                ++currentFrequency;
            } else if(counterFloatDays <= amountFloatDays) {
                ++counterFloatDays;
                currentFrequency -= frequency;
                setWorkTime(indexDay, floatTime);
            }
        }
    }

    // ----------------------------------------------- step 1 ----------------------------------------------------------
    // Рабочие часы за день в данном графике не 8 часов, а 7,2, поэтому пересчитываем месячную норму и перезаписываем ее
    @Override
    protected void setNormTime(double normTime) {
        final double FLOAT_TIME_IN_DAY = 7.2;
        final double STANDARD_TIME_IN_DAY = 8.0;

        int hoursInShortDays = 0;
        while(normTime % STANDARD_TIME_IN_DAY != 0) {
            ++normTime;
            ++hoursInShortDays;
        }
        super.setNormTime(FLOAT_TIME_IN_DAY * (normTime / STANDARD_TIME_IN_DAY) - hoursInShortDays);
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------
    // Т.к. данные графики имеют месячную норму с минутами,
    // к примеру, не 152 часа (19 дней * 8 часов), а 136,8 часа (19 дней * 7,2 часа),
    // для начала избавляемся от минут путем выставления 4-х смен по 7,2 (136,8 - 4 * 7,2 = 108)
    // а затем вызываем родительскую функцию генерации
    @Override
    protected void generateGraph(int daysInMonth) throws Exception {
        final double MINUTE_FOR_FLOAT_TIME = 0.2;
        int amountBlankDays = calcBlankDays(daysInMonth);
        double missingTime = calcMissingTime(daysInMonth);
        double averageWorkTime = amountBlankDays != 0 ? missingTime / amountBlankDays : missingTime;
        double floatTime = (int) averageWorkTime + MINUTE_FOR_FLOAT_TIME;

        int amountFloatDays = 0;
        while((missingTime - floatTime * amountFloatDays + 1.0e-10) % 1 > 0.001) ++amountFloatDays;

        if(amountFloatDays != 0) setFloatDay(floatTime, amountFloatDays, amountBlankDays, daysInMonth);
        super.generateGraph(daysInMonth);
    }
}