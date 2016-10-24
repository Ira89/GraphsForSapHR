package ru.polynkina.irina.graphs;

import ru.polynkina.irina.main.Main;

import java.util.Map;

public class GraphUnique extends Graph {

    private double uniqueTime;
    private String uniqueTimeSign;

    public GraphUnique(int id, String name, String rule, double daytime, String daytimeSign,
                double uniqueTime, String uniqueTimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
        this.uniqueTime = uniqueTime;
        this.uniqueTimeSign = uniqueTimeSign;
    }


    /*******************************************************************************************************************************************
                                                        getters and setters
     ******************************************************************************************************************************************/


    @Override
    public double getUniqueTime(){ return uniqueTime; }


    @Override
    public String getUniqueTimeSign(){ return uniqueTimeSign; }


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    @Override
    protected void setShortDayAndHolidays(final Map<Integer, Integer> shortDayAndHolidays){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getRuleOfDay(positionForRule) != CHAR_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()){
                    if(obj.getKey() == indexDay + 1){
                        if(obj.getValue() == CODE_SHORT_DAY){
                            if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime() - 1);
                            else if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime() - 1);
                        }
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
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime());
                else if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime());
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }
}
