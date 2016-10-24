package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

import java.util.Map;

public class GraphStandard extends Graph {

    public GraphStandard(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
    }


    @Override
    protected void setShortDayAndHolidays(final Map<Integer, Integer> shortDayAndHolidays){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getRuleOfDay(positionForRule) != CHAR_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()){
                    if(obj.getKey() == indexDay + 1){
                        if(obj.getValue() == CODE_SHORT_DAY) setWorkTime(indexDay, getDaytime() - 1);
                        else if(obj.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, 0);
                        else if(obj.getValue() == CODE_DAY_OFF) setWorkTime(indexDay, 0);
                    }
                }
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }


    @Override
    protected void generateGraph(final int amountUninitializedDays, final double sumTimesUninitializedDays){
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE) setWorkTime(indexDay, getDaytime());
        }
    }
}
