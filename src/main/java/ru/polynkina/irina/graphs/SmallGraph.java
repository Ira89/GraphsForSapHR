package ru.polynkina.irina.graphs;

import java.util.Map;

public class SmallGraph extends FiveDayGraph {

    public SmallGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------
    // getKey() -> получаем день месяца
    // getValue() -> получаем код этого дня (0 - сокращенный, 1 - праздничный, 2 - перенесенный)
    // Если день является выходным - ничего не делаем, т.к. нулевое рабочее время уже установлено на шаге 2
    // Если день является сокращенным - на 1 час не сокращаем,
    // т.к. к этому типу относятся графики, где рабочее время меньше 1 часа в день
    // Для графика типа SmallGraph ставим 0 часов на праздничные дни и перенесенные праздничные дни
    @Override
    protected void setShortAndHolidays(Map<Integer, Integer> shortAndHolidays) throws ArrayIndexOutOfBoundsException {
        for (Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if (getRuleOfDay(day.getKey()) == WEEKEND) continue;
            if (day.getValue() == CODE_HOLIDAY || day.getValue() == CODE_DAY_OFF) setWorkTime(day.getKey(), 0);
        }
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------
    // getKey() -> получаем день месяца
    // getValue() -> получаем код этого дня (0 - сокращенный, 1 - праздничный, 2 - перенесенный)
    // Признак праздничного дня устанавливается всегда
    // Признак сокращенного дня для данного типа графиков НЕ устанавливается
    // Для графика типа SmallGraph на перенесеннные праздничные дни устанавливается признак 'F'
    @Override
    protected void setShortAndHolidaysSign(Map<Integer, Integer> shortAndHolidays) throws ArrayIndexOutOfBoundsException {
        for(Map.Entry<Integer, Integer> obj : shortAndHolidays.entrySet()) {
            if(obj.getValue() == CODE_HOLIDAY) setHolidaysSign((obj.getKey()), SIGN_HOLIDAY);
            if(getRuleOfDay(obj.getKey()) != WEEKEND) {
                if(obj.getValue() == CODE_DAY_OFF || obj.getValue() == CODE_HOLIDAY) {
                    setShortDaysSign((obj.getKey()), SIGN_OFF_DAY);
                }
            }
        }
    }
}
