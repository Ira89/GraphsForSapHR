package ru.polynkina.irina.graphs;

import java.util.Map;

public class FiveDayGraph extends DayGraph {

    public FiveDayGraph(int id, String name, String rule, double daytime, String daytimeSign){
        super(id, name, rule, daytime, daytimeSign);
    }

    protected void overwriteNormTime() {
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
        overwriteNormTime();
    }

    @Override
    protected void setWorkTimeSign(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if (getRuleOfDay(indexDay) != SIGN_WEEKEND) {
                setWorkTimeSign(indexDay, getDaytimeSign());
            } else setWorkTimeSign(indexDay, findHourName(dayHours, 0));
        }
    }

    @Override
    protected void setHolidaysAndShortDaysSign(final Map<Integer, Integer> shortDayAndHolidays) {
        for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()) {
            if(obj.getValue() == CODE_HOLIDAY) {
                setHolidaysSign((obj.getKey() - 1), '1');
                setShortDaysSign((obj.getKey() - 1), 'F');
            } else if(obj.getValue() == CODE_SHORT_DAY && getRuleOfDay(obj.getKey() - 1) != SIGN_WEEKEND) {
                setShortDaysSign((obj.getKey() - 1), 'A');
            } else if(obj.getValue() == CODE_DAY_OFF && getRuleOfDay(obj.getKey() - 1) != SIGN_WEEKEND) {
                setShortDaysSign((obj.getKey() - 1), 'F');
            }
        }
    }
}
