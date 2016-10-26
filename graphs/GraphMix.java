package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

public class GraphMix extends Graph {

    private double nightTime;
    private String nightTimeSign;

    public GraphMix(int id, String name, String rule, double daytime, String daytimeSign,
             double nightTime, String nightTimeSign, final double NORM_TIME, final int DAY_OF_MONTH){
        super(id, name, rule, daytime, daytimeSign, NORM_TIME, DAY_OF_MONTH);
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
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH && amountSetting <= maxAmountSetting; ++indexDay){
            if(getRuleOfDay(indexDay) == dayType && getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                setWorkTime(indexDay, setValue);
                ++amountSetting;
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

        int minWorkTime = amountMissingDays == 0 ? (int) missingTime : (int) missingTime / amountMissingDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountMissingDays);
        int amountDaysWithMaxTime = amountMissingDays - amountDaysWithMinTime;

        int spreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int rareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountSpreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;

        char mostFrequentTypeOfDay;
        if(spreadValue > rareValue) mostFrequentTypeOfDay = Graph.CHAR_DESIGNATION_NIGHT;
        else mostFrequentTypeOfDay = Graph.CHAR_DESIGNATION_DAY;
        fillWorkTimeByType(mostFrequentTypeOfDay, spreadValue, amountSpreadValue);
        super.generateGraph();
    }
}