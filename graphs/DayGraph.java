package ru.polynkina.irina.graphs;

import java.util.Map;

public class DayGraph {

    final static double MAX_WORK_TIME_IN_DIURNAL = 22.0;
    final static double MAX_WORK_TIME_IN_DAY_TIME = 15.0;
    final static double STANDARD_TIME_IN_DAY = 8.0;
    final static double UNINITIALIZED_WORK_TIME = -1.0;

    final static int CODE_SHORT_DAY = 0;
    final static int CODE_HOLIDAY = 1;
    final static int CODE_DAY_OFF = 2;

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

    private double workTime[];
    private String workTimeSign[];
    private char holidaysSign[];
    private char shortDaysSign[];

    public DayGraph(int id, String name, String rule, double basicTime, String basicTimeSign) throws Exception {
        if(!ruleIsCorrect(rule)) throw new Exception("Поведение правила " + rule + " не определено");
        if(!timeIsCorrect(basicTime)) throw new Exception("Рабочее время не может принимать значение: " + basicTime);

        this.id = id;
        this.name = name;
        this.rule = rule;
        this.basicTime = basicTime;
        this.basicTimeSign = basicTimeSign;
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

    // ---------------------------------------------------- getters ----------------------------------------------------

    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getLengthRule(){ return rule.length(); }
    double getBasicTime(){ return basicTime; }
    String getBasicTimeSign(){ return basicTimeSign; }
    public int getCounter(){ return counter; }
    public double getNormTime(){ return normTime; }
    public int getAmountDay() { return workTime.length; }

    public double getWorkTime(int indexDay) throws Exception {
        if(indexDayIsCorrect(indexDay)) return workTime[indexDay];
        else throw new Exception("Индекс дня не может быть равен: " + indexDay);
    }

    public String getWorkTimeSign(final int indexDay) throws Exception {
        if(indexDayIsCorrect(indexDay)) return workTimeSign[indexDay];
        else throw new Exception("Индекс дня не может быть равен: " + indexDay);
    }

    public char getHolidaysSign(int indexDay) throws Exception {
        if(indexDayIsCorrect(indexDay)) return holidaysSign[indexDay];
        else throw new Exception("Индекс дня не может быть равен: " + indexDay);
    }

    public char getShortDaysSign(int indexDay) throws Exception {
        if(indexDayIsCorrect(indexDay)) return shortDaysSign[indexDay];
        else throw new Exception("Индекс дня не может быть равен: " + indexDay);
    }

    char getRuleOfDay(int indexDay) {
        int rulesPosition = (getCounter() + indexDay) % rule.length();
        if(rulesPosition < 0) rulesPosition = getLengthRule() + rulesPosition;
        return rule.charAt(rulesPosition);
    }

    public boolean isNightTime(int indexDay) throws Exception {
        if(indexDayIsCorrect(indexDay)) return getRuleOfDay(indexDay) == SIGN_NIGHT;
        else throw new Exception("Индекс " + indexDay + " выходит за пределы массива");
    }

    private boolean indexDayIsCorrect(int indexDay) { return indexDay >= 0 && indexDay < getAmountDay(); }

    // -------------------------------------------------- setters ------------------------------------------------------

    void setWorkTime(int indexDay, double time){ if(indexDayIsCorrect(indexDay)) workTime[indexDay] = time; }
    void setWorkTimeSign(int indexDay, String sign){ if(indexDayIsCorrect(indexDay)) workTimeSign[indexDay] = sign; }
    void setHolidaysSign(int indexDay, char sign) { if(indexDayIsCorrect(indexDay)) holidaysSign[indexDay] = sign; }
    void setShortDaysSign(int indexDay, char sign) { if(indexDayIsCorrect(indexDay)) shortDaysSign[indexDay] = sign; }

    public void setCounter(int counter) throws Exception {
        if(counter >= getLengthRule()) throw new Exception("Позиция счетчика не может быть больше, чем правило");
        this.counter = counter;
    }

    // ----------------------------------------------- generation ------------------------------------------------------

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

    private void createEmptyArrays(int dayInMonth) {
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

    private void setWeekend(){
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if(getRuleOfDay(indexDay) == SIGN_WEEKEND) setWorkTime(indexDay, 0);
        }
    }

    // ----------------------------------------------- step 3 ----------------------------------------------------------

    protected void setShortAndHolidays(final Map<Integer, Integer> shortDayAndHolidays) {
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
            if(getRuleOfDay(indexDay) != SIGN_WEEKEND) {
                Integer codeDay = shortAndHolidays.get(indexDay + 1);
                if(codeDay != null && codeDay == CODE_SHORT_DAY) ++hour;
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

    protected void setShortAndHolidaysSign(Map<Integer, Integer> shortAndHolidays) {
        for(Map.Entry<Integer, Integer> day : shortAndHolidays.entrySet()) {
            if(day.getValue() == CODE_HOLIDAY) setHolidaysSign((day.getKey() - 1), '1');
            else if(day.getValue() == CODE_SHORT_DAY && getRuleOfDay(day.getKey() - 1) != SIGN_WEEKEND)
                setShortDaysSign((day.getKey() - 1), 'A');
        }
    }
}