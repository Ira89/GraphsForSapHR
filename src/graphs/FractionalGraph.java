package ru.polynkina.irina.graphs;

public class FractionalGraph extends DayGraph {

    public FractionalGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    private void setFloatDay(double floatTime, int amountFloatDay, int amountMissingDays) throws Exception {
        double frequency = calcFrequency(amountFloatDay, amountMissingDays - amountFloatDay);
        double currentFrequency = 0;
        int amountOfAddedDays = 0;

        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) {
                if(currentFrequency < frequency) ++currentFrequency;
                else if(amountOfAddedDays <= amountFloatDay) {
                    ++amountOfAddedDays;
                    currentFrequency -= frequency;
                    setWorkTime(indexDay, floatTime);
                }
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

        int amountFloatDay = 0;
        double sumFloatHours = 0;
        while((missingTime - sumFloatHours) % 1 > 0.001) {
            sumFloatHours += floatTime;
            ++amountFloatDay;
        }

        if(amountFloatDay != 0) setFloatDay(floatTime, amountFloatDay, amountMissingDays);
        super.generateGraph();
    }
}