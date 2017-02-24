package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import java.util.Map;

public class FiveDayGraph extends DayGraph {

    public FiveDayGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    void overwriteNormTime(int daysInMonth) throws ArrayIndexOutOfBoundsException {
        double sumTime = 0;
        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay){
            sumTime += getWorkTime(indexDay);
        }
        super.setNormTime(sumTime);
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------
    // getKey() -> получаем день месяца
    // getValue() -> получаем код этого дня (0 - сокращенный, 1 - праздничный, 2 - перенесенный)
    // Если день является выходным - ничего не делаем, т.к. нулевое рабочее время уже установлено на шаге 2
    // Для графика типа FiveDayGraph ставим 0 часов на праздничные дни и перенесенные праздничные дни
    @Override
    protected void setShortAndHolidays(Map<Integer, Integer> shortAndHolidays) throws ArrayIndexOutOfBoundsException {
        for (Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if (getRuleOfDay(day.getKey()) == WEEKEND) continue;
            if (day.getValue() == CODE_SHORT_DAY) setWorkTime(day.getKey(), getBasicTime() - 1);
            else if (day.getValue() == CODE_HOLIDAY || day.getValue() == CODE_DAY_OFF) setWorkTime(day.getKey(), 0);
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------
    // На все незаполненные дни ставим стандартное рабочее время
    // и перезаписываем норму времени, т.к. она может не сходиться со стандартным (и это нормально для данного типа графика)
    @Override
    protected void generateGraph(int daysInMonth) throws ArrayIndexOutOfBoundsException {
        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) setWorkTime(indexDay, getBasicTime());
        }
        overwriteNormTime(daysInMonth);
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    // Ставим или стандартное время, или выходной
    @Override
    protected void setWorkTimeSign(int daysInMonth, Map<Integer, Integer> shortAndHolidays, Hours libHours) throws Exception {

        for (int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            if (getRuleOfDay(indexDay) != WEEKEND) setWorkTimeSign(indexDay, getBasicTimeSign());
            else setWorkTimeSign(indexDay, libHours.findSignDayHours(0));
        }
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------
    // getKey() -> получаем день месяца
    // getValue() -> получаем код этого дня (0 - сокращенный, 1 - праздничный, 2 - перенесенный)
    // Признак праздничного дня устанавливается всегда, а признак сокращенного - только для рабочих дней (с ненулевым временем)
    // Для графика типа FiveDayGraph (и наследников) на перенесеннные праздничные дни устанавливается признак 'F'
    @Override
    protected void setShortAndHolidaysSign(Map<Integer, Integer> shortAndHolidays) throws ArrayIndexOutOfBoundsException {
        for(Map.Entry<Integer, Integer> obj : shortAndHolidays.entrySet()) {
            if(obj.getValue() == CODE_HOLIDAY) setHolidaysSign((obj.getKey()), SIGN_HOLIDAY);
            if(getRuleOfDay(obj.getKey()) != WEEKEND) {
                if(obj.getValue() == CODE_SHORT_DAY) setShortDaysSign((obj.getKey()), SIGN_SHORT_DAY);
                if(obj.getValue() == CODE_DAY_OFF || obj.getValue() == CODE_HOLIDAY) {
                    setShortDaysSign((obj.getKey()), SIGN_OFF_DAY);
                }
            }
        }
    }
}