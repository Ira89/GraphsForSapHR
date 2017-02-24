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
    // getKey() -> получаем день месяца (т.к. пользователь вводит даты в обычном виде, а в массиве дни идут с нуля - разница в индексах на 1)
    // getValue() -> получаем код этого дня (0 - сокращенный, 1 - праздничный, 2 - перенесенный)
    // Если день является выходным - ничего не делаем, т.к. нулевое рабочее время уже установлено на шаге 2
    // На сокращенные дни ставим время на 1 час меньше основного или дополнительного ('d' или 'u' в rule)
    // Для графика типа UniqueGraph ставим 0 часов на праздничные дни и перенесенные праздничные дни
    @Override
    protected void setShortAndHolidays(Map<Integer, Integer> shortAndHolidays) throws ArrayIndexOutOfBoundsException {
        for (Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if (getRuleOfDay(day.getKey() - 1) == WEEKEND) continue;
            if (day.getValue() == CODE_SHORT_DAY) {
                if (getRuleOfDay(day.getKey() - 1) == DAY) setWorkTime(day.getKey() - 1, getBasicTime() - 1);
                else setWorkTime(day.getKey() - 1, getExtraTime() - 1);
            } else if(day.getValue() == CODE_HOLIDAY || day.getValue() == CODE_DAY_OFF) setWorkTime(day.getKey() - 1, 0);
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------
    // На все незаполненные дни ставим стандартное или дополнительное рабочее время ('d' или 'u' в rule)
    // и перезаписываем норму времени, т.к. она может не сходиться со стандартным (и это нормально для данного типа графиков)
    @Override
    protected void generateGraph(int daysInMonth) throws ArrayIndexOutOfBoundsException {
        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME){
                if(getRuleOfDay(indexDay) == DAY) setWorkTime(indexDay, getBasicTime());
                else setWorkTime(indexDay, getExtraTime());
            }
        }
        overwriteNormTime(daysInMonth);
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    // Ставим или стандартное время, дополнительное время, или выходной
    @Override
    protected void setWorkTimeSign(int daysInMonth, Map<Integer, Integer> shortAndHolidays, Hours libHours) throws Exception {

        for (int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            if (getRuleOfDay(indexDay) != WEEKEND) {
                if(getRuleOfDay(indexDay) == DAY) setWorkTimeSign(indexDay, getBasicTimeSign());
                else setWorkTimeSign(indexDay, getExtraTimeSign());
            } else setWorkTimeSign(indexDay, libHours.findSignDayHours(0));
        }
    }
}