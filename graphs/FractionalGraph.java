package ru.polynkina.irina.graphs;

public class FractionalGraph extends DayGraph {

    public FractionalGraph(int id, String name, String rule, double daytime, String daytimeSign) throws Exception {
        super(id, name, rule, daytime, daytimeSign);
    }

    private void setFloatDay(double floatTime, int counterFloatDay, int amountUninitializedDays) throws Exception {
        double frequency = calcFrequency(counterFloatDay, amountUninitializedDays);
        double currentFrequency = 0;
        int amountWorkDay = 0;

        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME){
                if(currentFrequency + 1 < frequency  && amountWorkDay < amountUninitializedDays - counterFloatDay){
                    ++currentFrequency;
                    ++amountWorkDay;
                }
                else{
                    setWorkTime(indexDay, floatTime);
                    currentFrequency = 0;
                }
            }
        }
    }

    @Override
    protected void setNormTime(double normTime){
        final double FLOAT_TIME_IN_DAY = 7.2;
        final double STANDARD_TIME_IN_DAY = 8.0;
        int hoursInShortDays = 0;
        while(normTime % STANDARD_TIME_IN_DAY != 0){
            ++normTime;
            ++hoursInShortDays;
        }
        double amountDay = normTime / STANDARD_TIME_IN_DAY;
        double newWorkTimeInMonth =  FLOAT_TIME_IN_DAY * amountDay - hoursInShortDays;
        super.setNormTime(newWorkTimeInMonth);
    }

    @Override
    protected void generateGraph() throws Exception {
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();
        final double MINUTE_FOR_FLOAT_TIME = 0.2;
        double averageWorkTime = missingTime;
        if(amountMissingDays != 0) averageWorkTime /= amountMissingDays;

        int minWorkTime = (int) averageWorkTime;
        int maxWorkTime = minWorkTime > averageWorkTime ? minWorkTime - 1 : minWorkTime + 1;
        double floatTime = minWorkTime < maxWorkTime ? minWorkTime + MINUTE_FOR_FLOAT_TIME : maxWorkTime + MINUTE_FOR_FLOAT_TIME;

        int counterFloatDay = 0;
        double sumFloatDay = 0;
        while((missingTime - sumFloatDay) % 1 > 0.001) {
            sumFloatDay += floatTime;
            ++counterFloatDay;
        }

        if(counterFloatDay != 0) setFloatDay(floatTime, counterFloatDay, amountMissingDays);
        super.generateGraph();
    }
}