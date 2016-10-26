package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

public class GraphDiurnal extends Graph {

    public GraphDiurnal(int id, String name, String rule, double daytime, String daytimeSign, final double NORM_TIME, final int DAY_OF_MONTH){
        super(id, name, rule, daytime, daytimeSign, NORM_TIME, DAY_OF_MONTH);
    }


    /*******************************************************************************************************************************************
                                                        private methods
     ******************************************************************************************************************************************/


    private void setAdditionalWorkDay(double additionalTime){
        final int INDEX_MIDDLE_DAY_OFF = 2;
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH && additionalTime != 0; ++indexDay){
            if(getRuleOfDay(indexDay) == CHAR_DESIGNATION_NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < Main.DAY_OF_MONTH){
                double additionalHours = additionalTime < MAX_WORK_TIME_IN_DAY ? additionalTime : MAX_WORK_TIME_IN_DAY;
                setWorkTime(indexDay + INDEX_MIDDLE_DAY_OFF, additionalHours);
                additionalTime -= additionalHours;
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
        double averageWorkTime = missingTime;
        if(amountMissingDays != 0) averageWorkTime /= amountMissingDays;

        double additionalTime = 0;
        if(averageWorkTime > MAX_WORK_TIME_IN_DIURNAL){
            additionalTime = missingTime - (amountMissingDays * MAX_WORK_TIME_IN_DIURNAL);
            setAdditionalWorkDay(additionalTime);
        }
        super.generateGraph();
    }
}