package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;

import java.util.Map;

public class DayGraph implements Graph {

    private int id;
    private String name;
    private String rule;
    private double basicTime;
    private String basicTimeSign;
    private int counter;
    private double normTime;
    private String text;

    private double workTime[];
    private String workTimeSign[];
    private char holidaysSign[];
    private char shortDaysSign[];

    public DayGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        if(!ruleIsCorrect(rule)) throw new Exception("Поведение правила " + rule + " не определено");
        if(!timeIsCorrect(basicTime)) throw new Exception("Рабочее время не может принимать значение: " + basicTime);

        this.id = id;
        this.name = name;
        this.rule = rule;
        this.basicTime = basicTime;
        this.basicTimeSign = basicTimeSign;
        this.text = text;
    }

    // --------------------------------------------- функции для проверок ----------------------------------------------

    private boolean ruleIsCorrect(String rule){
        for(int positionForRule = 0; positionForRule < rule.length(); ++positionForRule){
            switch(rule.charAt(positionForRule)){
                case UNIVERSAL_DAY: break;
                case WEEKEND: break;
                case NIGHT: break;
                case DAY: break;
                default: return false;
            }
        }
        return true;
    }

    boolean timeIsCorrect(double time) {
        return time > 0 && time <= MAX_WORK_TIME_IN_DIURNAL;
    }

    private void checkIndexOfDay(int indexDay) throws Exception {
        if(indexDay < 0 || indexDay >= workTime.length)
            throw new Exception("Индекс " + indexDay + " не входит в диапазон от 0 до " + workTime.length + "!");
    }

    public double calcRealNormTime() throws Exception {
        double sumHours = 0;
        for(int indexDay = 0; indexDay < workTime.length; ++indexDay) sumHours += getWorkTime(indexDay);
        return sumHours;
    }

    // ---------------------------------------------------- getters ----------------------------------------------------

    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getLengthRule(){ return rule.length(); }
    public double getBasicTime(){ return basicTime; }
    public String getBasicTimeSign(){ return basicTimeSign; }
    public String getText() { return text; }
    public int getCounter(){ return counter; }
    public double getNormTime(){ return normTime; }

    public double getWorkTime(int indexDay) throws Exception {
        checkIndexOfDay(indexDay);
        return workTime[indexDay];
    }

    public String getWorkTimeSign(int indexDay) throws Exception {
        checkIndexOfDay(indexDay);
        return workTimeSign[indexDay];
    }

    public char getHolidaysSign(int indexDay) throws Exception {
        checkIndexOfDay(indexDay);
        return holidaysSign[indexDay];
    }

    public char getShortDaysSign(int indexDay) throws Exception {
        checkIndexOfDay(indexDay);
        return shortDaysSign[indexDay];
    }

    public char getRuleOfDay(int indexDay) {
        int rulesPosition = (getCounter() + indexDay) % rule.length();
        if(rulesPosition < 0) rulesPosition = getLengthRule() + rulesPosition;
        return rule.charAt(rulesPosition);
    }

    // -------------------------------------------------- setters ------------------------------------------------------

    public void setWorkTime(int indexDay, double time) throws Exception {
        checkIndexOfDay(indexDay);
        workTime[indexDay] = time;
    }

    public void setWorkTimeSign(int indexDay, String sign) throws Exception {
        checkIndexOfDay(indexDay);
        workTimeSign[indexDay] = sign;
    }

    public void setHolidaysSign(int indexDay, char sign) throws Exception {
        checkIndexOfDay(indexDay);
        holidaysSign[indexDay] = sign;
    }

    public void setShortDaysSign(int indexDay, char sign) throws Exception {
        checkIndexOfDay(indexDay);
        shortDaysSign[indexDay] = sign;
    }

    public void setCounter(int counter) throws Exception {
        if(counter >= getLengthRule())
            throw new Exception("Позиция счетчика (" + counter + ") превышает длину правила (" + getLengthRule() + ")!");
        this.counter = counter;
    }

    // ----------------------------------------------- generation ------------------------------------------------------
    // facade
    public void startGenerating(ReportingPeriod period, Hours libHours) throws Exception {

        createEmptyArrays(period.getDaysInMonth());                     // step 0
        setNormTime(period.getNormTime());                              // step 1
        setWeekend(period);                                             // step 2
        setShortAndHolidays(period);                                    // step 3
        generateGraph(period);                                          // step 4
        setWorkTimeSign(period, libHours);                              // step 5
        setShortAndHolidaysSign(period);                                // step 6
    }

    // ----------------------------------------------- step 0 ----------------------------------------------------------

    private void createEmptyArrays(int dayInMonth) throws Exception {
        workTime = new double[dayInMonth];
        workTimeSign = new String[dayInMonth];
        holidaysSign = new char[dayInMonth];
        shortDaysSign = new char[dayInMonth];

        for(int indexDay = 0; indexDay < dayInMonth; ++indexDay) {
            setWorkTime(indexDay, UNINITIALIZED_WORK_TIME);
            setWorkTimeSign(indexDay, "");
            setHolidaysSign(indexDay, ' ');
            setShortDaysSign(indexDay, ' ');
        }
    }

    // ----------------------------------------------- step 1 ----------------------------------------------------------

    protected void setNormTime(double normTime){ this.normTime = normTime; }

    // ----------------------------------------------- step 2 ----------------------------------------------------------

    private void setWeekend(ReportingPeriod period) throws Exception {
        for(int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            if(getRuleOfDay(indexDay) == WEEKEND) setWorkTime(indexDay, 0);
        }
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------
    // Если день является выходным - ничего не делаем, т.к. нулевое рабочее время уже установлено на шаге 2
    // Индекс дня day.getKey() уменьшаем на 1, т.к. в массиве индексация с 0, а пользователь вводит числа в обычном виде
    protected void setShortAndHolidays(ReportingPeriod period) throws Exception {
        for(Map.Entry<Integer, Integer> day : period.getCopyShortAndHolidays().entrySet()) {
            if(getRuleOfDay(day.getKey() - 1) == WEEKEND) continue;
            if(day.getValue() == CODE_SHORT_DAY) setWorkTime(day.getKey() - 1, getBasicTime() - 1);
            else if(day.getValue() == CODE_HOLIDAY) setWorkTime(day.getKey() - 1, getBasicTime());
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------

    protected void generateGraph(ReportingPeriod period) throws Exception {
        int amountBlankDays = calcBlankDays(period);
        double missingTime = calcMissingTime(period);
        int minWorkTime = amountBlankDays == 0 ? (int) missingTime : (int) missingTime / amountBlankDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountBlankDays);
        int amountDaysWithMaxTime = amountBlankDays - amountDaysWithMinTime;
        double frequency = calcFrequency(amountDaysWithMinTime, amountDaysWithMaxTime);

        if(amountDaysWithMinTime >= amountDaysWithMaxTime)
            fillMissingWorkDays(minWorkTime, amountDaysWithMinTime, maxWorkTime, amountDaysWithMaxTime, frequency, period);
        else fillMissingWorkDays(maxWorkTime, amountDaysWithMaxTime, minWorkTime, amountDaysWithMinTime, frequency, period);
    }

    // Считаем дни, которые остались незаполненными на предыдущих этапах
    int calcBlankDays(ReportingPeriod period) throws Exception {
        int amountBlankDays = 0;
        for(int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) ++amountBlankDays;
        }
        return amountBlankDays;
    }

    // Считаем, сколько часов еще нужно, чтобы в итоге выйти на месячную норму времени
    double calcMissingTime(ReportingPeriod period) throws Exception {
        double missingTime = 0;
        for(int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            if(getWorkTime(indexDay) != UNINITIALIZED_WORK_TIME) missingTime += getWorkTime(indexDay);
        }
        return getNormTime() - missingTime;
    }

    // Допустим, минимальное время 6 часов, максимальное время 7 часов. Нужно выйти на 125 часов при 20 рабочих днях
    // Получится, что 6 часов * 15 дней + 7 часов * 5 дней = 125 часов
    // Возвращаем дни для минимального времени - т.к. 15 дней
    int calcDaysWithMinTime(int minWorkTime, int maxWorkTime, double sumWorkTime, int amountWorkDays) {
        int daysWithMinTime;
        for(daysWithMinTime = 0; daysWithMinTime <= amountWorkDays; ++daysWithMinTime) {
            int daysWithMaxTime = amountWorkDays - daysWithMinTime;
            if((daysWithMinTime * minWorkTime + daysWithMaxTime * maxWorkTime - sumWorkTime) < 0.001) break;
        }
        return daysWithMinTime;
    }

    // Если в функцию переданы значения (15 и 5) или (5 и 15), на выходе должны получить 3, т.е. 15 / 5
    double calcFrequency(int amountDaysWithMinTime, int amountDaysWithMaxTime) {
        if(amountDaysWithMinTime > amountDaysWithMaxTime)
            return amountDaysWithMaxTime == 0 ? amountDaysWithMinTime : (double) amountDaysWithMinTime / amountDaysWithMaxTime;
        return amountDaysWithMinTime == 0 ? amountDaysWithMaxTime : (double) amountDaysWithMaxTime / amountDaysWithMinTime;
    }

    // Незаполненные дни должны быть более или менее равномерно заполнены значениями 6 и 7 с частотой 3 к 1, т.е.
    // 6; 6; 6; 7; 6; 6; 6; 7... и т.д.
    private void fillMissingWorkDays(int spreadValue, int amountSpreadValue, int rareValue, int amountRareValue,
                                     double frequency, ReportingPeriod period) throws Exception {

        double currentFrequency = 0;
        int counterSpreadValue = 0;
        int counterRareValue = 0;

        for(int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            if(getWorkTime(indexDay) != UNINITIALIZED_WORK_TIME) continue;
            if(currentFrequency < frequency && counterSpreadValue < amountSpreadValue || counterRareValue == amountRareValue) {
                setWorkTime(indexDay, spreadValue);
                ++counterSpreadValue;
                ++currentFrequency;
            } else {
                setWorkTime(indexDay, rareValue);
                ++counterRareValue;
                currentFrequency -= frequency;
            }
        }
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------
    // Т.к. индексация массива с 0, а пользователь вводит даты в обычном виде - прибавляем 1 к indexDay при запросе codeDay
    // Если день является сокращенным - ищем однодневный график на час больше (сокращение на 1 час будет в шаге 6)
    protected void setWorkTimeSign(ReportingPeriod period, Hours libHours) throws Exception {
        for (int indexDay = 0; indexDay < period.getDaysInMonth(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = period.getCopyShortAndHolidays().get(indexDay + 1);
            if(codeDay != null) {
                if(getRuleOfDay(indexDay) != WEEKEND && codeDay == CODE_SHORT_DAY)
                    ++hour;
            }

            if (getBasicTime() == hour) setWorkTimeSign(indexDay, getBasicTimeSign());
            else setWorkTimeSign(indexDay, libHours.findSignDayHours(hour));
        }
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------
    // Индекс дня day.getKey() уменьшаем на 1, т.к. в массиве индексация с 0, а пользователь вводит числа в обычном виде
    protected void setShortAndHolidaysSign(ReportingPeriod period) throws Exception {
        for(Map.Entry<Integer, Integer> day : period.getCopyShortAndHolidays().entrySet()) {
            if(day.getValue() == CODE_HOLIDAY) {
                setHolidaysSign((day.getKey() - 1), SIGN_HOLIDAY);
            } else if(day.getValue() == CODE_SHORT_DAY && getRuleOfDay(day.getKey() - 1) != WEEKEND) {
                setShortDaysSign((day.getKey() - 1), SIGN_SHORT_DAY);
            }
        }
    }
}