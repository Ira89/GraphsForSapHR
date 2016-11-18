package ru.polynkina.irina.graphs;

import java.util.Map;

public class FiveDayGraph extends DayGraph {

    public FiveDayGraph(int id, String name, String rule, double basicTime, String basicTimeSign) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign);
    }

    void overwriteNormTime() throws Exception {
        double sumTime = 0;
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            sumTime += getWorkTime(indexDay);
        }
        super.setNormTime(sumTime);
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------
    @Override
    protected void setShortAndHolidays(final Map<Integer, Integer> shortAndHolidays) {
        for (Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if (getRuleOfDay(day.getKey() - 1) == SIGN_WEEKEND) continue;
            if (day.getValue() == CODE_SHORT_DAY) setWorkTime(day.getKey() - 1, getBasicTime() - 1);
            else if (day.getValue() == CODE_HOLIDAY || day.getValue() == CODE_DAY_OFF) setWorkTime(day.getKey() - 1, 0);
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------
    @Override
    protected void generateGraph() throws Exception {
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) setWorkTime(indexDay, getBasicTime());
        }
        overwriteNormTime();
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    @Override
    protected void setWorkTimeSign(Map<Integer, Integer> shortAndHolidays, Map<Double, String> dayHours,
                                   Map<Double, String> nightHours) throws Exception {

        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if (getRuleOfDay(indexDay) != SIGN_WEEKEND) setWorkTimeSign(indexDay, getBasicTimeSign());
            else setWorkTimeSign(indexDay, findHourName(dayHours, 0));
        }
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------
    @Override
    protected void setShortAndHolidaysSign(final Map<Integer, Integer> shortAndHolidays) {
        for(Map.Entry<Integer, Integer> obj : shortAndHolidays.entrySet()) {
            if(obj.getValue() == CODE_HOLIDAY) setHolidaysSign((obj.getKey() - 1), '1');
            if(getRuleOfDay(obj.getKey() - 1) != SIGN_WEEKEND) {
                if(obj.getValue() == CODE_SHORT_DAY) setShortDaysSign((obj.getKey() - 1), 'A');
                if(obj.getValue() == CODE_DAY_OFF || obj.getValue() == CODE_HOLIDAY) setShortDaysSign((obj.getKey() - 1), 'F');
            }
        }
    }
}