package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;

import java.util.Map;

public class FiveDayGraph extends DayGraph {

    public FiveDayGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    void overwriteNormTime(ReportingPeriod period) throws Exception {
        double sumTime = 0;
        for(int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay){
            sumTime += getWorkTime(indexDay);
        }
        super.setNormTime(sumTime);
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------
    // Если день является выходным - ничего не делаем, т.к. нулевое рабочее время уже установлено на шаге 2
    // Индекс дня day.getKey() уменьшаем на 1, т.к. в массиве индексация с 0, а пользователь вводит числа в обычном виде
    // Для стандартного пятидневного графика ставим 0 часов на выходные и перенесенные праздничные
    @Override
    protected void setShortAndHolidays(ReportingPeriod period) throws Exception {
        for (Map.Entry<Integer, Integer> day : period.getCopyShortAndHolidays().entrySet()) {
            if (getRuleOfDay(day.getKey() - 1) == WEEKEND) continue;
            if (day.getValue() == CODE_SHORT_DAY) setWorkTime(day.getKey() - 1, getBasicTime() - 1);
            else if (day.getValue() == CODE_HOLIDAY || day.getValue() == CODE_DAY_OFF) setWorkTime(day.getKey() - 1, 0);
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------
    // На все незаполненные дни ставим стандартное рабочее время
    // и перезаписываем норму времени, т.к. она может не сходиться со стандартным (и это нормально)
    @Override
    protected void generateGraph(ReportingPeriod period) throws Exception {
        for(int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) setWorkTime(indexDay, getBasicTime());
        }
        overwriteNormTime(period);
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    // Ставим или стандартное время, или выходной
    @Override
    protected void setWorkTimeSign(ReportingPeriod period, Hours libHours) throws Exception {

        for (int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            if (getRuleOfDay(indexDay) != WEEKEND) setWorkTimeSign(indexDay, getBasicTimeSign());
            else setWorkTimeSign(indexDay, libHours.findSignDayHours(0));
        }
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------
    // Индекс дня day.getKey() уменьшаем на 1, т.к. в массиве индексация с 0, а пользователь вводит числа в обычном виде
    // Только для данного типа графиков устанавливаются признаки перенесенных дней (SIGN_OF_DAY)
    @Override
    protected void setShortAndHolidaysSign(ReportingPeriod period) throws Exception {
        for(Map.Entry<Integer, Integer> obj : period.getCopyShortAndHolidays().entrySet()) {
            if(obj.getValue() == CODE_HOLIDAY) setHolidaysSign((obj.getKey() - 1), SIGN_HOLIDAY);
            if(getRuleOfDay(obj.getKey() - 1) != WEEKEND) {
                if(obj.getValue() == CODE_SHORT_DAY) setShortDaysSign((obj.getKey() - 1), SIGN_SHORT_DAY);
                if(obj.getValue() == CODE_DAY_OFF || obj.getValue() == CODE_HOLIDAY) {
                    setShortDaysSign((obj.getKey() - 1), SIGN_OFF_DAY);
                }
            }
        }
    }
}