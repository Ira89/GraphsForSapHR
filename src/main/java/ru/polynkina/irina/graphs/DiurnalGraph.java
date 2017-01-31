package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;

public class DiurnalGraph extends DayGraph {

    public DiurnalGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    // Суточные - это 1 рабочий через 3 выходных
    // Дополнительную смену будем ставить в середину, т.е. на второй выходной день
    // Дополнительная смена не может быть ночной, это смена дневная
    private void setAdditionalWorkDay(double additionalTime, ReportingPeriod period) throws Exception {
        final int INDEX_MIDDLE_DAY_OFF = 2;
        for(int indexDay = 0; indexDay < period.getDaysInMonth() && additionalTime != 0; ++indexDay) {
            if(getRuleOfDay(indexDay) == NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < period.getDaysInMonth()) {
                double hoursForCurrDay = additionalTime < MAX_WORK_TIME_IN_DAY_TIME ? additionalTime : MAX_WORK_TIME_IN_DAY_TIME;
                setWorkTime(indexDay + INDEX_MIDDLE_DAY_OFF, hoursForCurrDay);
                additionalTime -= hoursForCurrDay;
            }
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------
    // Т.к. в суточных графиках рабочее время может получиться меньше, чем месячная норма,
    // возможно, придется проставить дополнительные часы, на дни, которые рабочими не являются
    @Override
    protected void generateGraph(ReportingPeriod period) throws Exception {
        int amountBlankDays = calcBlankDays(period);
        double missingTime = calcMissingTime(period);
        double averageWorkTime = amountBlankDays != 0 ? missingTime / amountBlankDays : missingTime;

        if(averageWorkTime > MAX_WORK_TIME_IN_DIURNAL)
            setAdditionalWorkDay(missingTime - (amountBlankDays * MAX_WORK_TIME_IN_DIURNAL), period);
        super.generateGraph(period);
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    // Т.к. индексация массива с 0, а пользователь вводит даты в обычном виде - прибавляем 1 к indexDay при запросе codeDay
    // Если день является сокращенным - ищем однодневный график на час больше (сокращение на 1 час будет в шаге 6)
    // Если доп. смена попадает на сокращенный день - часы на 1 не увеличиваем, и сокращение на 1 час в шаге 6 не делаем
    @Override
    protected void setWorkTimeSign(ReportingPeriod period, Hours libHours) throws Exception {

        for (int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = period.getCopyShortAndHolidays().get(indexDay + 1);
            if (codeDay != null) {
                if (getRuleOfDay(indexDay) != WEEKEND && codeDay == CODE_SHORT_DAY) ++hour;
                if (codeDay == CODE_HOLIDAY
                        && getBasicTime() == hour
                        && getRuleOfDay(indexDay) == NIGHT && getRuleOfDay(indexDay - 1) == NIGHT) {
                    setWorkTimeSign(indexDay, SECOND_NIGHT_SHIFT);
                    continue;
                }
            }
            if(getRuleOfDay(indexDay) == NIGHT) {
                if(getBasicTime() == hour) setWorkTimeSign(indexDay, getBasicTimeSign());
                else setWorkTimeSign(indexDay, libHours.findSignNightHours(hour));
            } else setWorkTimeSign(indexDay, libHours.findSignDayHours(hour));
        }
    }
}