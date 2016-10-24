package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

public class GraphDiurnal extends Graph {

    public GraphDiurnal(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
    }


    /*******************************************************************************************************************************************
                                                        private methods
     ******************************************************************************************************************************************/


    private void setAdditionalWorkDay(double additionalTime){
        final int INDEX_MIDDLE_DAY_OFF = 2;
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH && additionalTime != 0; ++indexDay){
            if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < Main.DAY_OF_MONTH){
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
    protected void generateGraph(final int amountUninitializedDays, final double sumTimesUninitializedDays){
        double averageWorkTime = sumTimesUninitializedDays;
        if(amountUninitializedDays != 0) averageWorkTime /= amountUninitializedDays;

        double additionalTime = 0;
        if(averageWorkTime > MAX_WORK_TIME_IN_DIURNAL){
            additionalTime = sumTimesUninitializedDays - (amountUninitializedDays * MAX_WORK_TIME_IN_DIURNAL);
            setAdditionalWorkDay(additionalTime);
        }
        super.generateGraph(amountUninitializedDays, sumTimesUninitializedDays - additionalTime);
    }
}