package ru.polynkina.irina.graphs;

public class FractionalGraph extends DayGraph {

    public FractionalGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    private void setFloatDay(double floatTime, int amountFloatDays, int amountMissingDays) throws Exception {
        int amountIntegerDays = amountMissingDays - amountFloatDays;
        double frequency = calcFrequency(amountFloatDays, amountIntegerDays);

        double currentFrequency = 0;
        int counterFloatDays = 0;
        int counterIntegerDays = 0;

        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
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

    @Override
    protected void generateGraph() throws Exception {
        final double MINUTE_FOR_FLOAT_TIME = 0.2;
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();
        double averageWorkTime = amountMissingDays != 0 ? missingTime / amountMissingDays : missingTime;
        double floatTime = (int) averageWorkTime + MINUTE_FOR_FLOAT_TIME;

        int amountFloatDays = 0;
        while((missingTime - floatTime * amountFloatDays + 1.0e-10) % 1 > 0.001) ++amountFloatDays;

        if(amountFloatDays != 0) setFloatDay(floatTime, amountFloatDays, amountMissingDays);
        super.generateGraph();
    }
}