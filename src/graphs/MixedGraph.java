package ru.polynkina.irina.graphs;

import java.util.Map;

public class MixedGraph extends DayGraph {

    private double extraTime;
    private String extraTimeSign;

    public MixedGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text,
                      double extraTime, String extraTimeSign) throws Exception {

        super(id, name, rule, basicTime, basicTimeSign, text);
        if(!timeIsCorrect(extraTime)) throw new Exception("Рабочее время не может принимать значение: " + extraTime);
        this.extraTime = extraTime;
        this.extraTimeSign = extraTimeSign;
    }

    private void fillWorkTimeByType(char dayType, double setValue, int maxAmountSetting) throws Exception {
        int amountSetting = 0;
        for(int indexDay = 0; indexDay < getAmountDay() && amountSetting <= maxAmountSetting; ++indexDay) {
            if(getRuleOfDay(indexDay) == dayType && getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) {
                setWorkTime(indexDay, setValue);
                ++amountSetting;
            }
        }
    }

    // ---------------------------------------------------- getters ----------------------------------------------------

    private double getExtraTime() { return extraTime; }
    private String getExtraTimeSign(){ return extraTimeSign; }

    // ----------------------------------------------- step 4 ----------------------------------------------------------

    @Override
    protected void generateGraph() throws Exception {
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();
        int minWorkTime = amountMissingDays == 0 ? (int) missingTime : (int) missingTime / amountMissingDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountMissingDays);
        int amountDaysWithMaxTime = amountMissingDays - amountDaysWithMinTime;

        if(amountDaysWithMinTime >= amountDaysWithMaxTime)
            fillWorkTimeByType(SIGN_DAY, minWorkTime, amountDaysWithMinTime);
        else fillWorkTimeByType(SIGN_NIGHT, maxWorkTime, amountDaysWithMaxTime);
        super.generateGraph();
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------

    @Override
    protected void setWorkTimeSign(Map<Integer, Integer> shortAndHolidays, Map<Double, String> dayHours,
                                   Map<Double, String> nightHours) throws Exception {

        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = shortAndHolidays.get(indexDay + 1);
            if(codeDay != null) {
                if(getRuleOfDay(indexDay) != SIGN_WEEKEND && codeDay == CODE_SHORT_DAY) ++hour;
                if(codeDay == CODE_HOLIDAY && getExtraTime() == hour &&
                    getRuleOfDay(indexDay) == SIGN_NIGHT && getRuleOfDay(indexDay - 1) == SIGN_NIGHT) {
                        setWorkTimeSign(indexDay, SECOND_NIGHT_SHIFT);
                        continue;
                }
            }
            if(getRuleOfDay(indexDay) == SIGN_NIGHT) {
                if(getExtraTime() == hour) setWorkTimeSign(indexDay, getExtraTimeSign());
                else setWorkTimeSign(indexDay, findHourName(nightHours, hour));
            } else {
                if(getBasicTime() == hour) setWorkTimeSign(indexDay, getBasicTimeSign());
                else setWorkTimeSign(indexDay, findHourName(dayHours, hour));
            }
        }
    }
}