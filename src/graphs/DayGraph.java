package ru.polynkina.irina.graphs;

import java.util.Map;

public class DayGraph {

    public final static String DAY_TYPE = "DAY";
    public final static String SHORT_TYPE = "SHORT";
    public final static String FIVE_DAY_TYPE = "STANDARD";
    public final static String FRACTIONAL_TYPE = "FLOAT";
    public final static String DIURNAL_TYPE = "DIURNAL";
    public final static String UNIQUE_TYPE = "UNIQUE";
    public final static String MIXED_TYPE = "MIX";

    final static double MAX_WORK_TIME_IN_DIURNAL = 22.0;
    final static double MAX_WORK_TIME_IN_DAY_TIME = 15.0;
    final static double STANDARD_TIME_IN_DAY = 8.0;
    final static double UNINITIALIZED_WORK_TIME = -1.0;

    final static int CODE_SHORT_DAY = 0;
    final static int CODE_HOLIDAY = 1;
    final static int CODE_DAY_OFF = 2;

    final static String SECOND_NIGHT_SHIFT = "C_33";
    private final static char SIGN_UNIVERSAL_DAY = 'u';
    final static char SIGN_WEEKEND = 'f';
    final static char SIGN_NIGHT = 'n';
    final static char SIGN_DAY = 'd';


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

    private boolean ruleIsCorrect(String rule){
        for(int positionForRule = 0; positionForRule < rule.length(); ++positionForRule){
            switch(rule.charAt(positionForRule)){
                case SIGN_UNIVERSAL_DAY: break;
                case SIGN_WEEKEND: break;
                case SIGN_NIGHT: break;
                case SIGN_DAY: break;
                default: return false;
            }
        }
        return true;
    }

    boolean timeIsCorrect(double time) {
        return time > 0 && time <= MAX_WORK_TIME_IN_DIURNAL;
    }

    public boolean isNightTime(int indexDay) throws Exception {
        checkIndexOfDay(indexDay);
        return getRuleOfDay(indexDay) == SIGN_NIGHT;
    }

    public boolean isNonWorkingDay(int indexDay) throws Exception {
        checkIndexOfDay(indexDay);
        return getWorkTime(indexDay) != 0;
    }

    private void checkIndexOfDay(int indexDay) throws Exception {
        if(indexDay < 0 || indexDay >= getAmountDay())
            throw new Exception("Индекс " + indexDay + " выходит за пределы массива");
    }

    // ---------------------------------------------------- getters ----------------------------------------------------

    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getLengthRule(){ return rule.length(); }
    double getBasicTime(){ return basicTime; }
    String getBasicTimeSign(){ return basicTimeSign; }
    public String getText() { return text; }
    public int getCounter(){ return counter; }
    public double getNormTime(){ return normTime; }
    public int getAmountDay() { return workTime.length; }

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

    char getRuleOfDay(int indexDay) {
        int rulesPosition = (getCounter() + indexDay) % rule.length();
        if(rulesPosition < 0) rulesPosition = getLengthRule() + rulesPosition;
        return rule.charAt(rulesPosition);
    }

    // -------------------------------------------------- setters ------------------------------------------------------

    void setWorkTime(int indexDay, double time) throws Exception {
        checkIndexOfDay(indexDay);
        workTime[indexDay] = time;
    }

    void setWorkTimeSign(int indexDay, String sign) throws Exception {
        checkIndexOfDay(indexDay);
        workTimeSign[indexDay] = sign;
    }

    void setHolidaysSign(int indexDay, char sign) throws Exception {
        checkIndexOfDay(indexDay);
        holidaysSign[indexDay] = sign;
    }

    void setShortDaysSign(int indexDay, char sign) throws Exception {
        checkIndexOfDay(indexDay);
        shortDaysSign[indexDay] = sign;
    }

    public void setCounter(int counter) throws Exception {
        if(counter >= getLengthRule()) throw new Exception("Позиция счетчика не может быть больше, чем правило");
        this.counter = counter;
    }

    // ----------------------------------------------- generation ------------------------------------------------------
    // facade
    public void startGenerating(double normTime, int dayInMonth, Map<Integer, Integer> shortAndHolidays,
                                Map<Double, String> dayHours, Map<Double, String> nightHours) throws Exception {

        createEmptyArrays(dayInMonth);                                  // step 0
        setNormTime(normTime);                                          // step 1
        setWeekend();                                                   // step 2
        setShortAndHolidays(shortAndHolidays);                          // step 3
        generateGraph();                                                // step 4
        setWorkTimeSign(shortAndHolidays, dayHours, nightHours);        // step 5
        setShortAndHolidaysSign(shortAndHolidays);                      // step 6
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

    private void setWeekend() throws Exception {
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if(getRuleOfDay(indexDay) == SIGN_WEEKEND) setWorkTime(indexDay, 0);
        }
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------

    protected void setShortAndHolidays(Map<Integer, Integer> shortDayAndHolidays) throws Exception {
        for(Map.Entry<Integer, Integer> day : shortDayAndHolidays.entrySet()) {
            if(getRuleOfDay(day.getKey() - 1) == SIGN_WEEKEND) continue;
            if(day.getValue() == CODE_SHORT_DAY) setWorkTime(day.getKey() - 1, getBasicTime() - 1);
            else if(day.getValue() == CODE_HOLIDAY) setWorkTime(day.getKey() - 1, getBasicTime());
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------

    protected void generateGraph() throws Exception {
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();
        int minWorkTime = amountMissingDays == 0 ? (int) missingTime : (int) missingTime / amountMissingDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountMissingDays);
        int amountDaysWithMaxTime = amountMissingDays - amountDaysWithMinTime;
        double frequency = calcFrequency(amountDaysWithMinTime, amountDaysWithMaxTime);

        if(amountDaysWithMinTime >= amountDaysWithMaxTime)
            fillMissingWorkDays(minWorkTime, amountDaysWithMinTime, maxWorkTime, amountDaysWithMaxTime, frequency);
        else fillMissingWorkDays(maxWorkTime, amountDaysWithMaxTime, minWorkTime, amountDaysWithMinTime, frequency);
    }


    int calcMissingDays() throws Exception {
        int amountMissingDays = 0;
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if(getWorkTime(indexDay) == UNINITIALIZED_WORK_TIME) ++amountMissingDays;
        }
        return amountMissingDays;
    }

    double calcMissingTime() throws Exception {
        double missingTime = 0;
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if(getWorkTime(indexDay) != UNINITIALIZED_WORK_TIME) missingTime += getWorkTime(indexDay);
        }
        return getNormTime() - missingTime;
    }

    int calcDaysWithMinTime(int minWorkTime, int maxWorkTime, double sumWorkTime, int amountWorkDays) {
        int daysWithMinTime;
        for(daysWithMinTime = 0; daysWithMinTime <= amountWorkDays; ++daysWithMinTime) {
            int daysWithMaxTime = amountWorkDays - daysWithMinTime;
            if((daysWithMinTime * minWorkTime + daysWithMaxTime * maxWorkTime - sumWorkTime) < 0.001) break;
        }
        return daysWithMinTime;
    }

    double calcFrequency(int amountDaysWithMinTime, int amountDaysWithMaxTime) {
        if(amountDaysWithMinTime > amountDaysWithMaxTime)
            return amountDaysWithMaxTime == 0 ? amountDaysWithMinTime : (double) amountDaysWithMinTime / amountDaysWithMaxTime;
        return amountDaysWithMinTime == 0 ? amountDaysWithMaxTime : (double) amountDaysWithMaxTime / amountDaysWithMinTime;
    }

    private void fillMissingWorkDays(int spreadValue, int amountSpreadValue, int rareValue, int amountRareValue,
                                     double frequency) throws Exception {

        double currentFrequency = 0;
        int counterSpreadValue = 0;
        int counterRareValue = 0;

        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
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

    protected void setWorkTimeSign(Map<Integer, Integer> shortAndHolidays, Map<Double, String> dayHours,
                                   Map<Double, String> nightHours) throws Exception {

        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            Integer codeDay = shortAndHolidays.get(indexDay + 1);
            if(codeDay != null) {
                if(getRuleOfDay(indexDay) != SIGN_WEEKEND && codeDay == CODE_SHORT_DAY) ++hour;
            }

            if (getBasicTime() == hour) setWorkTimeSign(indexDay, getBasicTimeSign());
            else setWorkTimeSign(indexDay, findHourName(dayHours, hour));
        }
    }


    String findHourName(Map<Double, String> hours, double desiredValue) throws Exception {
        String hourName = hours.get(desiredValue);
        if(hourName == null) throw new Exception("Не найден график на " + desiredValue + " часов");
        return hourName;
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------

    protected void setShortAndHolidaysSign(Map<Integer, Integer> shortAndHolidays) throws Exception {
        for(Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if(day.getValue() == CODE_HOLIDAY) setHolidaysSign((day.getKey() - 1), '1');
            else if(day.getValue() == CODE_SHORT_DAY && getRuleOfDay(day.getKey() - 1) != SIGN_WEEKEND)
                setShortDaysSign((day.getKey() - 1), 'A');
        }
    }
}