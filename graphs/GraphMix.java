package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

public class GraphMix extends Graph {

    private double nightTime;
    private String nightTimeSign;

    public GraphMix(int id, String name, String rule, double daytime, String daytimeSign,
             double nightTime, String nightTimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
        this.nightTime = nightTime;
        this.nightTimeSign = nightTimeSign;
    }


    /*******************************************************************************************************************************************
                                                        getters and setters
     ******************************************************************************************************************************************/


    @Override
    public double getNightTime() { return nightTime; }


    @Override
    public String getNightTimeSign(){ return nightTimeSign; }


    /*******************************************************************************************************************************************
                                                        private methods
     ******************************************************************************************************************************************/


    private void fillWorkTimeByType(final char dayType, final double setValue, final int maxAmountSetting){
        int amountSetting = 0;
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH && amountSetting <= maxAmountSetting; ++indexDay){
            if(getRuleOfDay(positionForRule) == dayType && getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                setWorkTime(indexDay, setValue);
                ++amountSetting;
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    @Override
    protected void generateGraph(final int amountUninitializedDays, final double sumTimesUninitializedDays){
        double averageWorkTime = sumTimesUninitializedDays;
        if(amountUninitializedDays != 0) averageWorkTime /= amountUninitializedDays;

        int minWorkTime = (int) averageWorkTime;
        int maxWorkTime = minWorkTime + 1;
        int amountDaysWithMinTime = getAmountDaysWithMinTime(minWorkTime, maxWorkTime, sumTimesUninitializedDays, amountUninitializedDays);
        int amountDaysWithMaxTime = amountUninitializedDays - amountDaysWithMinTime;

        int spreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int rareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountSpreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;

        char mostFrequentTypeOfDay;
        if(spreadValue > rareValue) mostFrequentTypeOfDay = Graph.CHAR_DESIGNATION_NIGHT;
        else mostFrequentTypeOfDay = Graph.CHAR_DESIGNATION_DAY;
        fillWorkTimeByType(mostFrequentTypeOfDay, spreadValue, amountSpreadValue);

        int newAmountUninitializedDays = getAmountUninitializedDays();
        double sumTimeInitializedDays = getSumTimeInitializedDays();
        double newSumTimesUninitializedDays = getWorkTimeInMonth() - sumTimeInitializedDays;
        super.generateGraph(newAmountUninitializedDays, newSumTimesUninitializedDays);
    }
}