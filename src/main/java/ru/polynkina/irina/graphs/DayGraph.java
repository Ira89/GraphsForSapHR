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

    protected boolean timeIsCorrect(double time) {
        return time > 0 && time <= MAX_WORK_TIME_IN_DIURNAL;
    }

    private boolean indexDayIsNotValid(int indexDay) {
        return (indexDay < 0 || indexDay >= workTime.length);
    }

    private void throwException(int indexDay) {
        throw new ArrayIndexOutOfBoundsException(name + ": индекс может быть в диапазоне от 0 до " + workTime.length
                                                + "! Было получено значение " + indexDay);
    }

    public double calcRealNormTime() throws Exception {
        double sumHours = 0;
        for(int indexDay = 0; indexDay < workTime.length; sumHours += getWorkTime(indexDay), ++indexDay);
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

    public double getWorkTime(int indexDay) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
        return workTime[indexDay];
    }

    public String getWorkTimeSign(int indexDay) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
        return workTimeSign[indexDay];
    }

    public char getHolidaysSign(int indexDay) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
        return holidaysSign[indexDay];
    }

    public char getShortDaysSign(int indexDay) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
        return shortDaysSign[indexDay];
    }

    public char getRuleOfDay(int indexDay) {
        int rulesPosition = (getCounter() + indexDay) % rule.length();
        if(rulesPosition < 0) rulesPosition = getLengthRule() + rulesPosition;
        return rule.charAt(rulesPosition);
    }

    // -------------------------------------------------- setters ------------------------------------------------------

    public void setWorkTime(int indexDay, double time) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
        workTime[indexDay] = time;
    }

    public void setWorkTimeSign(int indexDay, String sign) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
        workTimeSign[indexDay] = sign;
    }

    public void setHolidaysSign(int indexDay, char sign) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
        holidaysSign[indexDay] = sign;
    }

    public void setShortDaysSign(int indexDay, char sign) throws ArrayIndexOutOfBoundsException {
        if(indexDayIsNotValid(indexDay)) throwException(indexDay);
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

        createEmptyArrays(period.getDaysInMonth());                                             // step 0
        setNormTime(period.getNormTime());                                                      // step 1
        setWeekend(period.getDaysInMonth());                                                    // step 2
        setShortAndHolidays(period.getCopyShortAndHolidays());                                  // step 3
        generateGraph(period.getDaysInMonth());                                                 // step 4
        setWorkTimeSign(period.getDaysInMonth(), period.getCopyShortAndHolidays(), libHours);   // step 5
        setShortAndHolidaysSign(period.getCopyShortAndHolidays());                              // step 6
    }

    // ----------------------------------------------- step 0 ----------------------------------------------------------

    private void createEmptyArrays(int daysInMonth) throws ArrayIndexOutOfBoundsException {
        workTime = new double[daysInMonth];
        workTimeSign = new String[daysInMonth];
        holidaysSign = new char[daysInMonth];
        shortDaysSign = new char[daysInMonth];

        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            setWorkTime(indexDay, UNINITIALIZED_WORK_TIME);
            setWorkTimeSign(indexDay, "");
            setHolidaysSign(indexDay, ' ');
            setShortDaysSign(indexDay, ' ');
        }
    }

    // ----------------------------------------------- step 1 ----------------------------------------------------------

    protected void setNormTime(double normTime){ this.normTime = normTime; }

    // ----------------------------------------------- step 2 ----------------------------------------------------------

    private void setWeekend(int daysInMonth) throws ArrayIndexOutOfBoundsException {
        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            if(getRuleOfDay(indexDay) == WEEKEND) setWorkTime(indexDay, 0);
        }
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------
    // getKey() -> получаем день месяца
    // getValue() -> получаем код этого дня (0 - сокращенный, 1 - праздничный, 2 - перенесенный)
    // Если день является выходным - ничего не делаем, т.к. нулевое рабочее время уже установлено на шаге 2
    // На сокращенные дни ставим время на 1 час меньше основного
    protected void setShortAndHolidays(Map<Integer, Integer> shortAndHolidays) throws ArrayIndexOutOfBoundsException {
        for(Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if(getRuleOfDay(day.getKey()) == WEEKEND) continue;
            if(day.getValue() == CODE_SHORT_DAY) setWorkTime(day.getKey(), getBasicTime() - 1);
            else if(day.getValue() == CODE_HOLIDAY) setWorkTime(day.getKey(), getBasicTime());
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------

    protected void generateGraph(int daysInMonth) throws Exception {
        int amountBlankDays = calcBlankDays(daysInMonth);
        double missingTime = calcMissingTime(daysInMonth);
        int minWorkTime = amountBlankDays == 0 ? (int) missingTime : (int) missingTime / amountBlankDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountBlankDays);
        int amountDaysWithMaxTime = amountBlankDays - amountDaysWithMinTime;
        double frequency = calcFrequency(amountDaysWithMinTime, amountDaysWithMaxTime);

        if(amountDaysWithMinTime >= amountDaysWithMaxTime)
            fillMissingWorkDays(minWorkTime, amountDaysWithMinTime, maxWorkTime, amountDaysWithMaxTime, frequency, daysInMonth);
        else fillMissingWorkDays(maxWorkTime, amountDaysWithMaxTime, minWorkTime, amountDaysWithMinTime, frequency, daysInMonth);
    }

    // Считаем дни, которые остались незаполненными на предыдущих этапах
    protected int calcBlankDays(int daysInMonth) throws ArrayIndexOutOfBoundsException {
        int amountBlankDays = 0;
        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) ++amountBlankDays;
        }
        return amountBlankDays;
    }

    // Считаем, сколько часов еще нужно, чтобы в итоге выйти на месячную норму времени
    protected double calcMissingTime(int daysInMonth) throws ArrayIndexOutOfBoundsException {
        double missingTime = 0;
        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            if(getWorkTime(indexDay) != UNINITIALIZED_WORK_TIME) missingTime += getWorkTime(indexDay);
        }
        return getNormTime() - missingTime;
    }

    // Допустим, минимальное время 6 часов, максимальное время 7 часов. Нужно выйти на 125 часов при 20 рабочих днях
    // Получится, что 6 часов * 15 дней + 7 часов * 5 дней = 125 часов
    // Возвращаем дни для минимального времени - т.е. 15 дней
    protected int calcDaysWithMinTime(int minWorkTime, int maxWorkTime, double sumWorkTime, int amountWorkDays) {
        int daysWithMinTime;
        for(daysWithMinTime = 0; daysWithMinTime <= amountWorkDays; ++daysWithMinTime) {
            int daysWithMaxTime = amountWorkDays - daysWithMinTime;
            if((daysWithMinTime * minWorkTime + daysWithMaxTime * maxWorkTime - sumWorkTime) < 0.001) break;
        }
        return daysWithMinTime;
    }

    // Если в функцию переданы значения (15 и 5) или (5 и 15), на выходе должны получить 3, т.е. 15 / 5
    protected double calcFrequency(int amountDaysWithMinTime, int amountDaysWithMaxTime) {
        if(amountDaysWithMinTime > amountDaysWithMaxTime)
            return amountDaysWithMaxTime == 0 ? amountDaysWithMinTime : (double) amountDaysWithMinTime / amountDaysWithMaxTime;
        return amountDaysWithMinTime == 0 ? amountDaysWithMaxTime : (double) amountDaysWithMaxTime / amountDaysWithMinTime;
    }

    // Незаполненные дни должны быть более или менее равномерно заполнены значениями 6 и 7 с частотой 3 к 1, т.е.
    // 6; 6; 6; 7; 6; 6; 6; 7... и т.д.
    private void fillMissingWorkDays(int spreadValue, int amountSpreadValue, int rareValue, int amountRareValue,
                                     double frequency, int daysInMonth) throws ArrayIndexOutOfBoundsException {

        double currentFrequency = 0;
        int counterSpreadValue = 0;
        int counterRareValue = 0;

        for(int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
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
    // Если день является сокращенным - ищем однодневный график на час больше (сокращение на 1 час будет в шаге 6)
    protected void setWorkTimeSign(int daysInMonth, Map<Integer, Integer> shortAndHolidays, Hours libHours) throws Exception {
        for (int indexDay = 0; indexDay < daysInMonth; ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = shortAndHolidays.get(indexDay);
            if(codeDay != null) {
                if(getRuleOfDay(indexDay) != WEEKEND && codeDay == CODE_SHORT_DAY) ++hour;
            }
            if (getBasicTime() == hour) setWorkTimeSign(indexDay, getBasicTimeSign());
            else setWorkTimeSign(indexDay, libHours.findSignDayHours(hour));
        }
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------
    // getKey() -> получаем день месяца
    // getValue() -> получаем код этого дня (0 - сокращенный, 1 - праздничный, 2 - перенесенный)
    // Признак праздничного дня устанавливается всегда, а признак сокращенного - только для рабочих дней (с ненулевым временем)
    protected void setShortAndHolidaysSign(Map<Integer, Integer> shortAndHolidays) throws ArrayIndexOutOfBoundsException {
        for(Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if(day.getValue() == CODE_HOLIDAY) {
                setHolidaysSign((day.getKey()), SIGN_HOLIDAY);
            } else if(day.getValue() == CODE_SHORT_DAY && getRuleOfDay(day.getKey()) != WEEKEND) {
                setShortDaysSign((day.getKey()), SIGN_SHORT_DAY);
            }
        }
    }
}