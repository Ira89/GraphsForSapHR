package ru.polynkina.irina.graphs;

public class GraphMix extends Graph {

    private double nightTime;
    private String nightTimeSign;


    GraphMix(int id, String name, String rule, double daytime, String daytimeSign,
             double nightTime, String nightTimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);

        try{
            boolean isCorrectTime = checkTimeInput(nightTime);
            if(!isCorrectTime) throw new Exception("Время не может принимать значение: " + nightTime);
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }

        this.nightTime = nightTime;
        this.nightTimeSign = nightTimeSign;
    }



    public void printInfo(){
        System.out.print("id: " + getId() + "\tname: " + getName() + "\trule: " + getRule() + "\tdaytime: " + getDaytime());
        System.out.print("\tdaytimeSign: " + getDaytimeSign() + "\tnightTime: " + nightTime + "\tnightTimeSign: " + nightTimeSign);
        System.out.println("\tworkTimeInMonth: " + getWorkTimeInMonth() + "\tcounter: " + getCounter());
    }


    /*******************************************************************************************************************************************
                                                        getters and setters
     ******************************************************************************************************************************************/


    public double getNightTime(){
        return nightTime;
    }


    public String getNightTimeSign(){
        return nightTimeSign;
    }


    /*******************************************************************************************************************************************
                                                        private methods
     ******************************************************************************************************************************************/


    private void fillWorkTimeByType(final char dayType, final double setValue, final int maxAmountSetting, final int AMOUNT_OF_DAYS){
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


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    public void generateGraph(final int amountUninitializedDays, final double sumTimesUninitializedDays, final int AMOUNT_OF_DAYS){
        double averageWorkTime = sumTimesUninitializedDays;
        if(amountUninitializedDays != 0) averageWorkTime /= amountUninitializedDays;

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

        int newAmountUninitializedDays = getAmountUninitializedDays(AMOUNT_OF_DAYS);
        double sumTimeInitializedDays = getSumTimeInitializedDays(AMOUNT_OF_DAYS);
        double newSumTimesUninitializedDays = getWorkTimeInMonth() - sumTimeInitializedDays;
        super.generateGraph(newAmountUninitializedDays, newSumTimesUninitializedDays, AMOUNT_OF_DAYS);
    }
}
