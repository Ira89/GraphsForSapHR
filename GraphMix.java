package ru.polynkina.irina.graphs;

public class GraphMix extends Graph {

    private double nightTime;
    private String nightTimeSign;

    GraphMix(int id, String name, String rule, double daytime, String daytimeSign, double nightTime, String nightTimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
        this.nightTime = nightTime;
        this.nightTimeSign = nightTimeSign;
    }


    public void printInfo(){
        System.out.print("id: " + getId() + "\tname: " + getName() + "\trule: " + getRule() + "\tdaytime: " + getDaytime() + "\tdaytimeSign: " + getDaytimeSign());
        System.out.println("\tnightTime: " + nightTime + "\tnightTimeSign: " + nightTimeSign + "\tworkTimeInMonth: " + getWorkTimeInMonth() + "\tcounter: " + getCounter());
    }


    public double getNightTime(){
        return nightTime;
    }


    public String getNightTimeSign(){
        return nightTimeSign;
    }


    private void fillWorkTimeByType(char dayType, double setValue, int maxAmountSetting, final int AMOUNT_OF_DAYS){
        int amountSetting = 0;
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();
        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS && amountSetting <= maxAmountSetting; ++indexDay){
            if(getRuleOfDay(positionForRule) == dayType && getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                setWorkTime(indexDay, setValue);
                ++amountSetting;
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }


    public void generateGraph(final int AMOUNT_OF_DAYS, int amountUninitializedDays, double sumTimesUninitializedDays){
        double averageWorkTime;
        if(amountUninitializedDays != 0) averageWorkTime = sumTimesUninitializedDays / amountUninitializedDays;
        else averageWorkTime = sumTimesUninitializedDays;

        int minWorkTime = (int) averageWorkTime;
        int maxWorkTime = minWorkTime > averageWorkTime ? minWorkTime - 1 : minWorkTime + 1;
        int amountDaysWithMinTime = getAmountDaysWithMinTime(minWorkTime, maxWorkTime, sumTimesUninitializedDays, amountUninitializedDays);
        int amountDaysWithMaxTime = amountUninitializedDays - amountDaysWithMinTime;

        int spreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int rareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountSpreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;

        char mostFrequentTypeOfDay;
        if(spreadValue > rareValue) mostFrequentTypeOfDay = Graph.CHAR_DESIGNATION_NIGHT;
        else mostFrequentTypeOfDay = Graph.CHAR_DESIGNATION_DAY;
        fillWorkTimeByType(mostFrequentTypeOfDay, spreadValue, amountSpreadValue, AMOUNT_OF_DAYS);

        amountUninitializedDays = getAmountUninitializedDays(AMOUNT_OF_DAYS);
        double sumTimeInitializedDays = getSumTimeInitializedDays(AMOUNT_OF_DAYS);
        sumTimesUninitializedDays = getWorkTimeInMonth() - sumTimeInitializedDays;
        super.generateGraph(AMOUNT_OF_DAYS, amountUninitializedDays, sumTimesUninitializedDays);
    }
}
