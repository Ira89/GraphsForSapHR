package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphUnique extends Graph {

    private double uniqueTime;
    private String uniqueTimeSign;

    GraphUnique(int id, String name, String rule, double daytime, String daytimeSign, double uniqueTime, String uniqueTimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
        this.uniqueTime = uniqueTime;
        this.uniqueTimeSign = uniqueTimeSign;
    }


    public void printInfo(){
        System.out.print("id: " + getId() + "\tname: " + getName() + "\trule: " + getRule() + "\tdaytime: " + getDaytime() + "\tdaytimeSign: " + getDaytimeSign());
        System.out.println("\tuniqueTime: " + uniqueTime + "\tuniqueTimeSign: " + uniqueTimeSign + "\tworkTimeInMonth: " + getWorkTimeInMonth() + "\tcounter: " + getCounter());
    }


    public double getUniqueTime(){
        return uniqueTime;
    }


    public String getUniqueTimeSign(){
        return uniqueTimeSign;
    }


    public void setShortDayAndHolidays(final int AMOUNT_OF_DAYS, final Map<Integer, Integer> shortDayAndHolidays){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
            if(getRuleOfDay(positionForRule) != CHAR_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> container : shortDayAndHolidays.entrySet()){
                    if(container.getKey() == indexDay + 1){
                        if(container.getValue() == CODE_SHORT_DAY){
                            if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime() - 1);
                            else if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime() - 1);
                        }
                        else if(container.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, 0);
                        else if(container.getValue() == CODE_DAY_OFF) setWorkTime(indexDay, 0);
                    }
                }
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }


    public void generateGraph(final int AMOUNT_OF_DAYS, int amountUninitializedDays, double sumTimesUninitializedDays){
        int lengthRule = getLengthRule();
        int currentCounter = getCounter();
        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(getRuleOfDay(currentCounter) == CHAR_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime());
                else if(getRuleOfDay(currentCounter) == CHAR_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime());
            }
            if(++currentCounter == lengthRule) currentCounter = 0;
        }
    }


}
