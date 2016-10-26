package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

public class GraphFloat extends Graph {

    public GraphFloat(int id, String name, String rule, double daytime, String daytimeSign, final double NORM_TIME, final int DAY_OF_MONTH){
        super(id, name, rule, daytime, daytimeSign, NORM_TIME, DAY_OF_MONTH);
        setNormTimeForStandardGraphs(NORM_TIME);
    }


    /*******************************************************************************************************************************************
                                                        private methods
     ******************************************************************************************************************************************/


    private void setNormTimeForStandardGraphs(double standardNormTimeInMonth){
        int hoursInShortDays = 0;
        while(standardNormTimeInMonth % STANDARD_TIME_IN_DAY != 0){
            ++standardNormTimeInMonth;
            ++hoursInShortDays;
        }
        double amountDay = standardNormTimeInMonth / STANDARD_TIME_IN_DAY;
        double newWorkTimeInMonth =  FLOAT_TIME_IN_DAY * amountDay - hoursInShortDays;
        setNormTime(newWorkTimeInMonth);
    }



    private void setFloatDay(final double floatTime, final int counterFloatDay, final int amountUninitializedDays){
        double frequency = calcFrequency(counterFloatDay, amountUninitializedDays);
        double currentFrequency = 0;
        int amountWorkDay = 0;

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
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


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    @Override
    protected void generateGraph(){
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
        while((missingTime - sumFloatDay + ACCEPTABLE_ACCURACY) % 1 > NEGLIGIBLE_TIME_INTERVAL){
            sumFloatDay += floatTime;
            ++counterFloatDay;
        }

        if(counterFloatDay != 0) setFloatDay(floatTime, counterFloatDay, amountMissingDays);
        super.generateGraph();
    }
}