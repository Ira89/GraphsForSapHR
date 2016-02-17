package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphStandard extends Graph {

    GraphStandard(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
    }


    public void setShortDayAndHolidays(final int AMOUNT_OF_DAYS, final Map<Integer, Integer> shortDayAndHolidays){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
            if(getRuleOfDay(positionForRule) != CHAR_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> container : shortDayAndHolidays.entrySet()){
                    if(container.getKey() == indexDay + 1){
                        if(container.getValue() == CODE_SHORT_DAY) setWorkTime(indexDay, getDaytime() - 1);
                        else if(container.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, 0);
                        else if(container.getValue() == CODE_DAY_OFF) setWorkTime(indexDay, 0);
                    }
                }
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }


    public void generateGraph(final int AMOUNT_OF_DAYS, int amountUninitializedDays, double sumTimesUninitializedDays){
        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE) setWorkTime(indexDay, getDaytime());
        }
    }
}
