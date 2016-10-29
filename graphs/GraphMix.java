package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphMix extends Graph {

    private double nightTime;
    private String nightTimeSign;

    public GraphMix(int id, String name, String rule, double daytime, String daytimeSign, double nightTime, String nightTimeSign){
        super(id, name, rule, daytime, daytimeSign);
        this.nightTime = nightTime;
        this.nightTimeSign = nightTimeSign;
    }

    private double getNightTime() { return nightTime; }
    private String getNightTimeSign(){ return nightTimeSign; }


    private void fillWorkTimeByType(final char dayType, final double setValue, final int maxAmountSetting){
        int amountSetting = 0;
        for(int indexDay = 0; indexDay < getAmountDay() && amountSetting <= maxAmountSetting; ++indexDay){
            if(getRuleOfDay(indexDay) == dayType && getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                setWorkTime(indexDay, setValue);
                ++amountSetting;
            }
        }
    }

    @Override
    protected void generateGraph(){
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();

        int minWorkTime = amountMissingDays == 0 ? (int) missingTime : (int) missingTime / amountMissingDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountMissingDays);
        int amountDaysWithMaxTime = amountMissingDays - amountDaysWithMinTime;

        int spreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int rareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountSpreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;

        char mostFrequentTypeOfDay;
        if(spreadValue > rareValue) mostFrequentTypeOfDay = SIGN_NIGHT;
        else mostFrequentTypeOfDay = SIGN_DAY;
        fillWorkTimeByType(mostFrequentTypeOfDay, spreadValue, amountSpreadValue);
        super.generateGraph();
    }

    @Override
    protected void setWorkTimeSign(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        final String SECOND_NIGHT_SHIFT_FOR_HOLIDAYS = "C_33";
        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = shortDayAndHolidays.get(indexDay + 1);
            if(getWorkTime(indexDay) != 0) {
                if(codeDay != null && codeDay == CODE_SHORT_DAY) ++hour;
            }
            if(getRuleOfDay(indexDay) == SIGN_NIGHT) {
                if(codeDay != null && codeDay == CODE_HOLIDAY && getRuleOfDay(indexDay - 1) == SIGN_NIGHT) {
                    setWorkTimeSign(indexDay, SECOND_NIGHT_SHIFT_FOR_HOLIDAYS);
                }
                else if(getNightTime() == hour) setWorkTimeSign(indexDay, getNightTimeSign());
                else setWorkTimeSign(indexDay, findHourName(nightHours, hour));
            } else {
                if(getDaytime() == hour) setWorkTimeSign(indexDay, getDaytimeSign());
                else setWorkTimeSign(indexDay, findHourName(dayHours, hour));
            }
        }
    }
}