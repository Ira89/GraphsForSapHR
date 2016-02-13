package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphsUnique extends Graph {

    private double uniqueTime;
    private String uniqueTimeSign;

    GraphsUnique(int id, String name, String rule, double daytime, String daytimeSign, double uniqueTime, String uniqueTimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
        this.uniqueTime = uniqueTime;
        this.uniqueTimeSign = uniqueTimeSign;
    }


    public double getUniqueTime(){
        return uniqueTime;
    }


    public String getUniqueTimeSign(){
        return uniqueTimeSign;
    }


    public void setShortDayAndHolidays(int amountDay, Map<Integer, Integer> shortDayAndHolidays){
        int lengthRule = getLengthRule();
        int currentCounter = getCounter();

        for(int indexDay = 0; indexDay < amountDay; ++indexDay){
            if(getRuleOfDay(currentCounter) != TYPE_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> container : shortDayAndHolidays.entrySet()){
                    if(container.getKey() == indexDay + 1){
                        if(container.getValue() == CODE_SHORT_DAY){
                            if(getRuleOfDay(currentCounter) == TYPE_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime() - 1);
                            else if(getRuleOfDay(currentCounter) == TYPE_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime() - 1);
                        }
                        else if(container.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, 0);
                        else if(container.getValue() == CODE_DAY_OFF) setWorkTime(indexDay, 0);
                    }
                }
            }
            if(++currentCounter == lengthRule) currentCounter = 0;
        }
    }


    public void generateGraph(int amountDay, int amountUninitializedDays, double sumTimesUninitializedDays){
        int lengthRule = getLengthRule();
        int currentCounter = getCounter();
        for(int indexDay = 0; indexDay < amountDay; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(getRuleOfDay(currentCounter) == TYPE_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime());
                else if(getRuleOfDay(currentCounter) == TYPE_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime());
            }
            if(++currentCounter == lengthRule) currentCounter = 0;
        }
    }


}
