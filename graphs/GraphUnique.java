package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphUnique extends Graph {

    private double uniqueTime;
    private String uniqueTimeSign;

    public GraphUnique(int id, String name, String rule, double daytime, String daytimeSign, double uniqueTime, String uniqueTimeSign){
        super(id, name, rule, daytime, daytimeSign);
        this.uniqueTime = uniqueTime;
        this.uniqueTimeSign = uniqueTimeSign;
    }

    private double getUniqueTime() { return uniqueTime; }
    private String getUniqueTimeSign(){ return uniqueTimeSign; }

    private void overwriteNormTime() {
        double sumTime = 0;
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            sumTime += getWorkTime(indexDay);
        }
        super.setNormTime(sumTime);
    }

    @Override
    protected void setShortDaysAndHolidays(final Map<Integer, Integer> shortDayAndHolidays){
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            if(getRuleOfDay(indexDay) != SIGN_WEEKEND){
                for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()){
                    if(obj.getKey() == indexDay + 1){
                        if(obj.getValue() == CODE_SHORT_DAY){
                            if(getRuleOfDay(indexDay) == SIGN_DAY) setWorkTime(indexDay, getDaytime() - 1);
                            else if(getRuleOfDay(indexDay) == SIGN_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime() - 1);
                        }
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
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(getRuleOfDay(indexDay) == SIGN_DAY) setWorkTime(indexDay, getDaytime());
                else if(getRuleOfDay(indexDay) == SIGN_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime());
            }
        }
        overwriteNormTime();
    }

    @Override
    protected void setWorkTimeSign(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if (getRuleOfDay(indexDay) != SIGN_WEEKEND) {
                if(getRuleOfDay(indexDay) == 'd') setWorkTimeSign(indexDay, getDaytimeSign());
                else setWorkTimeSign(indexDay, getUniqueTimeSign());
            } else setWorkTimeSign(indexDay, findHourName(dayHours, 0));
        }
    }
}