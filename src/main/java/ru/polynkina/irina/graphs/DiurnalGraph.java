package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;

public class DiurnalGraph extends DayGraph {

    public DiurnalGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    private void setAdditionalWorkDay(double additionalTime, ReportingPeriod period) throws Exception {
        final int INDEX_MIDDLE_DAY_OFF = 2;
        for(int indexDay = 0; indexDay < period.getDaysInMonth() && additionalTime != 0; ++indexDay) {
            if(getRuleOfDay(indexDay) == SIGN_NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < period.getDaysInMonth()) {
                double hoursFofCurrDay = additionalTime < MAX_WORK_TIME_IN_DAY_TIME ? additionalTime : MAX_WORK_TIME_IN_DAY_TIME;
                setWorkTime(indexDay + INDEX_MIDDLE_DAY_OFF, hoursFofCurrDay);
                additionalTime -= hoursFofCurrDay;
            }
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------

    @Override
    protected void generateGraph(ReportingPeriod period) throws Exception {
        int amountMissingDays = calcMissingDays(period);
        double missingTime = calcMissingTime(period);
        double averageWorkTime = amountMissingDays != 0 ? missingTime / amountMissingDays : missingTime;

        if(averageWorkTime > MAX_WORK_TIME_IN_DIURNAL)
            setAdditionalWorkDay(missingTime - (amountMissingDays * MAX_WORK_TIME_IN_DIURNAL), period);
        super.generateGraph(period);
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------

    @Override
    protected void setWorkTimeSign(ReportingPeriod period, Hours libHours) throws Exception {

        for (int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = period.getCopyShortAndHolidays().get(indexDay + 1);
            if (codeDay != null) {
                if (getRuleOfDay(indexDay) != SIGN_WEEKEND && codeDay == CODE_SHORT_DAY) ++hour;
                if (codeDay == CODE_HOLIDAY && getBasicTime() == hour &&
                        getRuleOfDay(indexDay) == SIGN_NIGHT && getRuleOfDay(indexDay - 1) == SIGN_NIGHT) {
                    setWorkTimeSign(indexDay, SECOND_NIGHT_SHIFT);
                    continue;
                }
            }
            if(getRuleOfDay(indexDay) == SIGN_NIGHT) {
                if(getBasicTime() == hour) setWorkTimeSign(indexDay, getBasicTimeSign());
                else setWorkTimeSign(indexDay, libHours.findSignNightHours(hour));
            } else setWorkTimeSign(indexDay, libHours.findSignDayHours(hour));
        }
    }
}