package ru.polynkina.irina.graphs;

public class GraphDiurnal extends Graph {

    final static int INDEX_MIDDLE_DAY_OFF = 2;
    final static double MAX_WORK_TIME_IN_DAY = 22;


    GraphDiurnal(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
    }



    private void setAdditionalWorkDay(double additionalTime, final int AMOUNT_OF_DAYS){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS && additionalTime != 0; ++indexDay){
            if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < AMOUNT_OF_DAYS){
                double additionalHours = additionalTime < MAX_WORK_TIME_IN_DAY ? additionalTime : MAX_WORK_TIME_IN_DAY;
                setWorkTime(indexDay + INDEX_MIDDLE_DAY_OFF, additionalHours);
                additionalTime -= additionalHours;
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }



    public void generateGraph(int amountUninitializedDays, double sumTimesUninitializedDays, final int AMOUNT_OF_DAYS){
        double averageWorkTime = sumTimesUninitializedDays;
        if(amountUninitializedDays != 0) averageWorkTime /= amountUninitializedDays;

        if(averageWorkTime > MAX_WORK_TIME_IN_DAY){
            double additionalTime = sumTimesUninitializedDays - (amountUninitializedDays * MAX_WORK_TIME_IN_DAY);
            setAdditionalWorkDay(additionalTime, AMOUNT_OF_DAYS);
            sumTimesUninitializedDays -= additionalTime;
        }
        super.generateGraph(amountUninitializedDays, sumTimesUninitializedDays, AMOUNT_OF_DAYS);
    }
}
