package ru.polynkina.irina.graphs;

import java.util.Map;

public class DiurnalGraph extends DayGraph {

    public DiurnalGraph(int id, String name, String rule, double daytime, String daytimeSign) throws Exception {
        super(id, name, rule, daytime, daytimeSign);
    }

    private void setAdditionalWorkDay(double additionalTime){
        final int INDEX_MIDDLE_DAY_OFF = 2;
        for(int indexDay = 0; indexDay < getAmountDay() && additionalTime != 0; ++indexDay){
            if(getRuleOfDay(indexDay) == SIGN_NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < getAmountDay()){
                double additionalHours = additionalTime < MAX_WORK_TIME_IN_DAY_TIME ? additionalTime : MAX_WORK_TIME_IN_DAY_TIME;
                setWorkTime(indexDay + INDEX_MIDDLE_DAY_OFF, additionalHours);
                additionalTime -= additionalHours;
            }
        }
    }

    @Override
    protected void generateGraph() throws Exception {
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
    protected void setWorkTimeSign(Map<Integer, Integer> shortAndHolidays, Map<Double, String> dayHours,
                                   Map<Double, String> nightHours) throws Exception {
        final String SECOND_NIGHT_SHIFT_FOR_HOLIDAYS = "C_33";
        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = shortAndHolidays.get(indexDay + 1);
            if(getRuleOfDay(indexDay) != SIGN_WEEKEND) {
                if(codeDay != null && codeDay == CODE_SHORT_DAY) ++hour;
            }
            if(getRuleOfDay(indexDay) == SIGN_NIGHT) {
                if(codeDay != null && codeDay == CODE_HOLIDAY && getRuleOfDay(indexDay - 1) == SIGN_NIGHT) {
                    setWorkTimeSign(indexDay, SECOND_NIGHT_SHIFT_FOR_HOLIDAYS);
                }
                else if (hour == getBasicTime()) setWorkTimeSign(indexDay, getBasicTimeSign());
                else setWorkTimeSign(indexDay, findHourName(nightHours, hour));
            } else setWorkTimeSign(indexDay, findHourName(dayHours, hour));
        }
    }
}