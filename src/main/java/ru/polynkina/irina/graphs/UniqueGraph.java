package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;

import java.util.Map;

public class UniqueGraph extends FiveDayGraph {

    private double extraTime;
    private String extraTimeSign;

    public UniqueGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text,
                       double extraTime, String extraTimeSign) throws Exception {

        super(id, name, rule, basicTime, basicTimeSign, text);
        if(!timeIsCorrect(extraTime)) throw new Exception("Рабочее время не может принимать значение: " + extraTime);
        this.extraTime = extraTime;
        this.extraTimeSign = extraTimeSign;
    }

    // ---------------------------------------------------- getters ----------------------------------------------------

    private double getExtraTime() { return extraTime; }
    private String getExtraTimeSign(){ return extraTimeSign; }

    // ----------------------------------------------- step 3 ----------------------------------------------------------
    @Override
    protected void setShortAndHolidays(ReportingPeriod period) throws Exception {
        for (Map.Entry<Integer, Integer> day : period.getCopyShortAndHolidays().entrySet()) {
            if (getRuleOfDay(day.getKey() - 1) == SIGN_WEEKEND) continue;
            if (day.getValue() == CODE_SHORT_DAY) {
                if (getRuleOfDay(day.getKey() - 1) == SIGN_DAY) setWorkTime(day.getKey() - 1, getBasicTime() - 1);
                else setWorkTime(day.getKey() - 1, getExtraTime() - 1);
            } else if(day.getValue() == CODE_HOLIDAY || day.getValue() == CODE_DAY_OFF) setWorkTime(day.getKey() - 1, 0);
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------
    @Override
    protected void generateGraph() throws Exception {
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME){
                if(getRuleOfDay(indexDay) == SIGN_DAY) setWorkTime(indexDay, getBasicTime());
                else setWorkTime(indexDay, getExtraTime());
            }
        }
        overwriteNormTime();
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    @Override
    protected void setWorkTimeSign(ReportingPeriod period, Hours libHours) throws Exception {

        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if (getRuleOfDay(indexDay) != SIGN_WEEKEND) {
                if(getRuleOfDay(indexDay) == SIGN_DAY) setWorkTimeSign(indexDay, getBasicTimeSign());
                else setWorkTimeSign(indexDay, getExtraTimeSign());
            } else setWorkTimeSign(indexDay, libHours.findSignDayHours(0));
        }
    }
}