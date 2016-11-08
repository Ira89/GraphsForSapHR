package ru.polynkina.irina.graphs;

import java.util.Map;

public class DayGraph {

    final static double UNINITIALIZED_VALUE = -1.0;
    final static int CODE_SHORT_DAY = 0;
    final static int CODE_HOLIDAY = 1;
    final static int CODE_DAY_OFF = 2;
    final static char SIGN_UNIVERSAL_DAY = 'u';
    final static char SIGN_WEEKEND = 'f';
    final static char SIGN_NIGHT = 'n';
    final static char SIGN_DAY = 'd';

    private int id;
    private String name;
    private String rule;
    private double daytime;
    private String daytimeSign;
    private int counter;
    private double normTime;

    private double workTime[];
    private String workTimeSign[];
    private String holidaysSign[];
    private String shortDaysSign[];

    public DayGraph(int id, String name, String rule, double daytime, String daytimeSign) {
        this.id = id;
        this.name = name;
        this.rule = rule;
        this.daytime = daytime;
        this.daytimeSign = daytimeSign;
    }

    // ----------------------------------------------- getters and setters ---------------------------------------------

    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getLengthRule(){ return rule.length(); }
    protected double getDaytime(){ return daytime; }
    protected String getDaytimeSign(){ return daytimeSign; }
    public int getCounter(){ return counter; }
    public double getNormTime(){ return normTime; }
    public int getAmountDay() { return workTime.length; }


    public double getWorkTime(final int indexDay){
        if(indexDayIsCorrect(indexDay)) return workTime[indexDay];
        else return -1.0;
    }

    public String getWorkTimeSign(final int indexDay) {
        if(indexDayIsCorrect(indexDay)) return workTimeSign[indexDay];
        else return "";
    }

    public String getHolidaysSign(int indexDay) {
        if(indexDayIsCorrect(indexDay)) return holidaysSign[indexDay];
        else return "";
    }

    public String getShortDaysSign(int indexDay) {
        if(indexDayIsCorrect(indexDay)) return shortDaysSign[indexDay];
        else return "";
    }

    protected void setWorkTime(int indexDay, double time){
        if(indexDayIsCorrect(indexDay)) workTime[indexDay] = time;
    }

    protected void setWorkTimeSign(int indexDay, String sign){
        if(indexDayIsCorrect(indexDay)) workTimeSign[indexDay] = sign;
    }

    protected void setHolidaysSign(int indexDay, String sign) {
        if(indexDayIsCorrect(indexDay)) holidaysSign[indexDay] = sign;
    }

    protected void setShortDaysSign(int indexDay, String sign) {
        if(indexDayIsCorrect(indexDay)) shortDaysSign[indexDay] = sign;
    }

    private boolean indexDayIsCorrect(int indexDay) {
        try {
            if(indexDay < 0 || indexDay >= getAmountDay())
                throw new Exception("Дата должна быть в диапазаоне от 1 до " + getAmountDay());
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(0);
        }
        return true;
    }

    public char getRuleOfDay(final int indexDay) {
        int rulesPosition = counter + indexDay;
        rulesPosition %= rule.length();
        if(rulesPosition < 0) rulesPosition = rule.length() + rulesPosition;
        return rule.charAt(rulesPosition);
    }

    public void setCounter(final int counter){
        try{
            if(counter >= getLengthRule())
                throw new Exception("Позиция счетчика не может быть больше, чем длина правила");
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        this.counter = counter;
    }

    /*private boolean checkRuleInput(final String rule){
        int sizeRule = rule.length();
        for(int positionForRule = 0; positionForRule < sizeRule; ++positionForRule){
            switch(rule.charAt(positionForRule)){
                case SIGN_UNIVERSAL_DAY: break;
                case SIGN_WEEKEND: break;
                case SIGN_NIGHT: break;
                case SIGN_DAY: break;
                default: return false;
            }
        }
        return true;
    }*/

    // ----------------------------------------------- generation ------------------------------------------------------

    public void startGenerating(double normTime, int dayOfMonth, final Map<Integer, Integer> shortDayAndHolidays,
                                final Map<Double, String> dayHours, final Map<Double, String> nightHours) {
        createEmptyArray(dayOfMonth);                                  // step 0
        setNormTime(normTime);                                         // step 1
        setWeekend();                                                  // step 2
        setShortDaysAndHolidays(shortDayAndHolidays);                  // step 3
        generateGraph();                                               // step 4
        setWorkTimeSign(shortDayAndHolidays, dayHours, nightHours);    // step 5
        setHolidaysAndShortDaysSign(shortDayAndHolidays);              // step 6
    }

    // ----------------------------------------------- step 0 ----------------------------------------------------------

    private void createEmptyArray(int dayOfMonth) {
        workTime = new double[dayOfMonth];
        workTimeSign = new String[dayOfMonth];
        holidaysSign = new String[dayOfMonth];
        shortDaysSign = new String[dayOfMonth];
        for(int indexDay = 0; indexDay < dayOfMonth; ++indexDay) {
            setWorkTime(indexDay, UNINITIALIZED_VALUE);
            setWorkTimeSign(indexDay, "");
            setHolidaysSign(indexDay, "");
            setShortDaysSign(indexDay, "");
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

    protected void setShortDaysAndHolidays(final Map<Integer, Integer> shortDayAndHolidays) {
        for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()) {
            if(getRuleOfDay(obj.getKey() - 1) != SIGN_WEEKEND) {
                if(obj.getValue() == CODE_SHORT_DAY) setWorkTime(obj.getKey() - 1, getDaytime() - 1);
                else if(obj.getValue() == CODE_HOLIDAY) setWorkTime(obj.getKey() - 1, getDaytime());
            }
        }
    }

    // ----------------------------------------------- step 4 ----------------------------------------------------------

    protected void generateGraph() {
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();
        int minWorkTime = amountMissingDays == 0 ? (int) missingTime : (int) missingTime / amountMissingDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountMissingDays);
        int amountDaysWithMaxTime = amountMissingDays - amountDaysWithMinTime;
        double frequency = calcFrequency(amountDaysWithMinTime, amountDaysWithMaxTime);

        if(amountDaysWithMinTime >= amountDaysWithMaxTime) {
            fillMissingWorkDays(minWorkTime, amountDaysWithMinTime, maxWorkTime, amountDaysWithMaxTime, frequency);
        } else fillMissingWorkDays(maxWorkTime, amountDaysWithMaxTime, minWorkTime, amountDaysWithMinTime, frequency);
    }

    protected int calcMissingDays(){
        int amountMissingDays = 0;
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE) ++amountMissingDays;
        }
        return amountMissingDays;
    }

    protected double calcMissingTime(){
        double missingTime = 0;
        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            if(getWorkTime(indexDay) != UNINITIALIZED_VALUE) missingTime += getWorkTime(indexDay);
        }
        return getNormTime() - missingTime;
    }

    protected int calcDaysWithMinTime(int minWorkTime, int maxWorkTime, double sumWorkTime, int amountWorkDays) {
        int daysWithMinTime;
        for(daysWithMinTime = 0; daysWithMinTime <= amountWorkDays; ++daysWithMinTime) {
            int daysWithMaxTime = amountWorkDays - daysWithMinTime;
            if((daysWithMinTime * minWorkTime + daysWithMaxTime * maxWorkTime - sumWorkTime) < 0.001) break;
        }
        return daysWithMinTime;
    }

    protected double calcFrequency(int amountDaysWithMinTime, int amountDaysWithMaxTime) {
        if(amountDaysWithMinTime > amountDaysWithMaxTime)
            return amountDaysWithMaxTime == 0 ? amountDaysWithMinTime : (double) amountDaysWithMinTime / amountDaysWithMaxTime;
        return amountDaysWithMinTime == 0 ? amountDaysWithMaxTime : (double) amountDaysWithMaxTime / amountDaysWithMinTime;
    }

    private void fillMissingWorkDays(int spreadValue, int amountSpreadValue, int rareValue, int amountRareValue, double frequency) {
        double currentFrequency = 0;
        int counterSpreadTime = 0;
        int counterRareTime = 0;

        for(int indexDay = 0; indexDay < getAmountDay(); ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(currentFrequency < frequency && counterSpreadTime < amountSpreadValue || counterRareTime == amountRareValue) {
                    setWorkTime(indexDay, spreadValue);
                    ++counterSpreadTime;
                    ++currentFrequency;
                }else{
                    setWorkTime(indexDay, rareValue);
                    ++counterRareTime;
                    currentFrequency -= frequency;
                }
            }
        }
    }

    // ----------------------------------------------- step 5 ----------------------------------------------------------

    protected void setWorkTimeSign(final Map<Integer, Integer> shortDayAndHolidays, final Map<Double, String> dayHours,
                                   final Map<Double, String> nightHours) {
        for (int indexDay = 0; indexDay < getAmountDay(); ++indexDay) {
            double hour = getWorkTime(indexDay);
            if(getRuleOfDay(indexDay) != SIGN_WEEKEND) {
                Integer codeDay = shortDayAndHolidays.get(indexDay + 1);
                if(codeDay != null && codeDay == CODE_SHORT_DAY) ++hour;
            }
            if (getDaytime() == hour) setWorkTimeSign(indexDay, getDaytimeSign());
            else setWorkTimeSign(indexDay, findHourName(dayHours, hour));
        }
    }

    protected static String findHourName(final Map<Double, String> hours, double desiredValue) {
        String hourName = hours.get(desiredValue);
        if(hourName == null){
            System.out.println("Нет графика на: " + desiredValue + " час");
            hourName = "???";
        }
        return hourName;
    }

    // ----------------------------------------------- step 6 ----------------------------------------------------------

    protected void setHolidaysAndShortDaysSign(final Map<Integer, Integer> shortDayAndHolidays) {
        for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()) {
            if(obj.getValue() == CODE_HOLIDAY) {
                setHolidaysSign((obj.getKey() - 1), "1");
            } else if(obj.getValue() == CODE_SHORT_DAY && getRuleOfDay(obj.getKey() - 1) != SIGN_WEEKEND) {
                setShortDaysSign((obj.getKey() - 1), "A");
            }
        }
    }
}