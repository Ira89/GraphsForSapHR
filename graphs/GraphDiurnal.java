package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphDiurnal extends Graph {

    private final static double MAX_WORK_TIME_IN_DAY = 15;
    private final static double MAX_WORK_TIME_IN_DIURNAL = 22;

    public GraphDiurnal(int id, String name, String rule, double daytime, String daytimeSign){
        super(id, name, rule, daytime, daytimeSign);
    }

    private void setAdditionalWorkDay(double additionalTime){
        final int INDEX_MIDDLE_DAY_OFF = 2;
        for(int indexDay = 0; indexDay < getAmountDay() && additionalTime != 0; ++indexDay){
            if(getRuleOfDay(indexDay) == SIGN_NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < getAmountDay()){
                double additionalHours = additionalTime < MAX_WORK_TIME_IN_DAY ? additionalTime : MAX_WORK_TIME_IN_DAY;
                setWorkTime(indexDay + INDEX_MIDDLE_DAY_OFF, additionalHours);
                additionalTime -= additionalHours;
            }
        }
    }

    @Override
    protected void generateGraph(){
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();
        double averageWorkTime = missingTime;
        if(amountMissingDays != 0) averageWorkTime /= amountMissingDays;

        if(averageWorkTime > MAX_WORK_TIME_IN_DIURNAL){
            setAdditionalWorkDay(missingTime - (amountMissingDays * MAX_WORK_TIME_IN_DIURNAL));
        }
        super.generateGraph();
    }

    @Override
    protected void setWorkTimeSign(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            if(getWorkTime(indexDay) != 0) {
                Integer codeDay = shortDayAndHolidays.get(indexDay + 1);
                if(codeDay != null && codeDay == CODE_SHORT_DAY) ++hour;
            }
            if(getRuleOfDay(indexDay) == SIGN_NIGHT) {
                if (hour == getDaytime()) setWorkTimeSign(indexDay, getDaytimeSign());
                else setWorkTimeSign(indexDay, findHourName(nightHours, hour));
            } else setWorkTimeSign(indexDay, findHourName(dayHours, hour));
        }
    }
}