package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;

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

    private void fillWorkTimeByType(char dayType, double setValue, int maxAmountSetting, ReportingPeriod period) throws Exception {
        int amountSetting = 0;
        for(int indexDay = 0; indexDay < period.getDaysInMonth() && amountSetting <= maxAmountSetting; ++indexDay) {
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
    // Данный тип графиков подразумевает и ночные, и дневные смены
    // На ночные смены нужно по возможности поставить бОльшие часы
    // К примеру, в месяце 20 рабочих дней, и из них 10 дневных, и 10 ночных
    //
    // Если распределение часов: 6 часов * 15 дней + 7 часов * 5 дней = 125 часов
    // То вначале на все Дневные смены в процедуре fillWorkTimeByType ставим по 6 часов,
    // А затем запускаем родительскую функцию генерации
    // И на ночные смены будет гарантировано проставлены все 5 смен по 7 часов ( и 5 смен по 6 часов)
    //
    // Если распределение часов: 7 часов * 15 дней + 6 часов * 5 дней = 135 часов
    // То вначале на все Ночные смены в процедуре fillWorkTimeByType ставим по 7 часов
    // А затем запускаем родительскую функцию генерации
    // В таком случае на все ночные смены мы поставим гарантированно бОльшие часы (все смены по 7 часов)
    @Override
    protected void generateGraph(ReportingPeriod period) throws Exception {
        int amountBlankDays = calcBlankDays(period);
        double missingTime = calcMissingTime(period);
        int minWorkTime = amountBlankDays == 0 ? (int) missingTime : (int) missingTime / amountBlankDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountBlankDays);
        int amountDaysWithMaxTime = amountBlankDays - amountDaysWithMinTime;

        if(amountDaysWithMinTime >= amountDaysWithMaxTime)
            fillWorkTimeByType(DAY, minWorkTime, amountDaysWithMinTime, period);
        else fillWorkTimeByType(NIGHT, maxWorkTime, amountDaysWithMaxTime, period);
        super.generateGraph(period);
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    // Т.к. индексация массива с 0, а пользователь вводит даты в обычном виде - прибавляем 1 к indexDay при запросе codeDay
    // Если день является сокращенным - ищем однодневный график на час больше (сокращение на 1 час будет в шаге 6)
    // Если перед праздничным ночным тоже была ночная смена - нужно использовать "C_33", а не искать в libHours
    @Override
    protected void setWorkTimeSign(ReportingPeriod period, Hours libHours) throws Exception {

        for (int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = period.getCopyShortAndHolidays().get(indexDay + 1);
            if(codeDay != null) {
                if(getRuleOfDay(indexDay) != WEEKEND && codeDay == CODE_SHORT_DAY) ++hour;
                if(codeDay == CODE_HOLIDAY
                        && getExtraTime() == hour
                        && getRuleOfDay(indexDay) == NIGHT
                        && getRuleOfDay(indexDay - 1) == NIGHT) {
                    setWorkTimeSign(indexDay, SECOND_NIGHT_SHIFT);
                    continue;
                }
            }
            if(getRuleOfDay(indexDay) == NIGHT) {
                if(getExtraTime() == hour) setWorkTimeSign(indexDay, getExtraTimeSign());
                else setWorkTimeSign(indexDay, libHours.findSignNightHours(hour));
            } else {
                if(getBasicTime() == hour) setWorkTimeSign(indexDay, getBasicTimeSign());
                else setWorkTimeSign(indexDay, libHours.findSignDayHours(hour));
            }
        }
    }
}