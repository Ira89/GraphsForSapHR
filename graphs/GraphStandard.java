package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphStandard extends Graph {

    public GraphStandard(int id, String name, String rule, double daytime, String daytimeSign){
        super(id, name, rule, daytime, daytimeSign);
    }

    @Override
    protected void setNormTime(double normTime){
        final double STANDARD_TIME_IN_DAY = 8.0;
        int hoursInShortDays = 0;
        while(normTime % STANDARD_TIME_IN_DAY != 0){
            ++normTime;
            ++hoursInShortDays;
        }
        double amountDay = normTime / STANDARD_TIME_IN_DAY;
        double newWorkTimeInMonth =  getDaytime() * amountDay - hoursInShortDays;
        super.setNormTime(newWorkTimeInMonth);
    }

    @Override
    protected void setShortDaysAndHolidays(final Map<Integer, Integer> shortDayAndHolidays){
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            if(getRuleOfDay(indexDay) != CHAR_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()){
                    if(obj.getKey() == indexDay + 1){
                        if(obj.getValue() == CODE_SHORT_DAY) setWorkTime(indexDay, getDaytime() - 1);
                        else if(obj.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, 0);
                        else if(obj.getValue() == CODE_DAY_OFF) setWorkTime(indexDay, 0);
                    }
                }
            }
        }
    }

    @Override
    protected void generateGraph(){
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE) setWorkTime(indexDay, getDaytime());
        }
    }
}
