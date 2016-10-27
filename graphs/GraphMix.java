package ru.polynkina.irina.graphs;

public class GraphMix extends Graph {

    private double nightTime;
    private String nightTimeSign;

    public GraphMix(int id, String name, String rule, double daytime, String daytimeSign, double nightTime, String nightTimeSign){
        super(id, name, rule, daytime, daytimeSign);
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
        for(int indexDay = 0; indexDay < getAmountDay() && amountSetting <= maxAmountSetting; ++indexDay){
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
        if(spreadValue > rareValue) mostFrequentTypeOfDay = CHAR_DESIGNATION_NIGHT;
        else mostFrequentTypeOfDay = CHAR_DESIGNATION_DAY;
        fillWorkTimeByType(mostFrequentTypeOfDay, spreadValue, amountSpreadValue);
        super.generateGraph();
    }
}