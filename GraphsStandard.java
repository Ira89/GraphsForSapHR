package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphsStandard extends Graph {

    GraphsStandard(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
    }


    public void setShortDayAndHolidays(int amountDay, Map<Integer, Integer> shortDayAndHolidays){
        int lengthRule = getLengthRule();
        int currentCounter = getCounter();

        for(int indexDay = 0; indexDay < amountDay; ++indexDay){
            if(getRuleOfDay(currentCounter) != TYPE_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> container : shortDayAndHolidays.entrySet()){
                    if(container.getKey() == indexDay + 1){
                        if(container.getValue() == CODE_SHORT_DAY) setWorkTime(indexDay, getDaytime() - 1);
                        else if(container.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, 0);
                        else if(container.getValue() == CODE_DAY_OFF) setWorkTime(indexDay, 0);
                    }
                }
            }
            if(++currentCounter == lengthRule) currentCounter = 0;
        }
    }


    public void generateGraph(int amountDay, int amountUninitializedDays, double sumTimesUninitializedDays){
        for(int indexDay = 0; indexDay < amountDay; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE) setWorkTime(indexDay, getDaytime());
        }
    }
}
