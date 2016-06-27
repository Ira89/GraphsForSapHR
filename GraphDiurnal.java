package ru.polynkina.irina.graphs;

public class GraphDiurnal extends Graph {

    final static int INDEX_MIDDLE_DAY_OFF = 2;

    GraphDiurnal(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
    }


    /*******************************************************************************************************************************************
                                                        private methods
     ******************************************************************************************************************************************/


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


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    @Override
    public void generateGraph(final int amountUninitializedDays, final double sumTimesUninitializedDays, final int AMOUNT_OF_DAYS){
        double averageWorkTime = sumTimesUninitializedDays;
        if(amountUninitializedDays != 0) averageWorkTime /= amountUninitializedDays;

        double additionalTime = 0;
        if(averageWorkTime > MAX_WORK_TIME_IN_DIURNAL){
            additionalTime = sumTimesUninitializedDays - (amountUninitializedDays * MAX_WORK_TIME_IN_DIURNAL);
            setAdditionalWorkDay(additionalTime, AMOUNT_OF_DAYS);
        }
        super.generateGraph(amountUninitializedDays, sumTimesUninitializedDays - additionalTime, AMOUNT_OF_DAYS);
    }
}