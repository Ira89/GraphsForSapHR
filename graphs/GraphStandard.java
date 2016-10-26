package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

import java.util.Map;

public class GraphStandard extends Graph {

    public GraphStandard(int id, String name, String rule, double daytime, String daytimeSign, final double NORM_TIME, final int DAY_OF_MONTH){
        super(id, name, rule, daytime, daytimeSign, NORM_TIME, DAY_OF_MONTH);
    }

    @Override
    protected void setShortDaysAndHolidays(final Map<Integer, Integer> shortDayAndHolidays){
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
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
}
