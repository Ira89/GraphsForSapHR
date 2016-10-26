package ru.polynkina.irina.graphs;

import java.util.Map;

public class Graph {

    final static double UNINITIALIZED_VALUE = -1;
    final static double STANDARD_TIME_IN_DAY = 8;
    final static double FLOAT_TIME_IN_DAY = 7.2;
    final static double MAX_WORK_TIME_IN_DAY = 15;
    final static double MAX_WORK_TIME_IN_DIURNAL = 22;

    final static double NEGLIGIBLE_TIME_INTERVAL = 0.001;
    final static double ACCEPTABLE_ACCURACY = 1.0e-10;

    public final static char CHAR_DESIGNATION_UNIVERSAL_DAY = 'u';
    public final static char CHAR_DESIGNATION_WEEKEND = 'f';
    public final static char CHAR_DESIGNATION_NIGHT = 'n';
    public final static char CHAR_DESIGNATION_DAY = 'd';

    public final static int CODE_SHORT_DAY = 0;
    public final static int CODE_HOLIDAY = 1;
    public final static int CODE_DAY_OFF = 2;


    private int id;
    private String name;
    private String rule;
    private double daytime;
    private String daytimeSign;
    private int counter;
    private double normTime;
    private double workTime[];
    private String workTimeSign[];


    public Graph(int id, String name, String rule, double daytime, String daytimeSign, final double NORM_TIME, final int DAY_OF_MONTH) {
        this.id = id;
        this.name = name;
        this.rule = rule;
        this.daytime = daytime;
        this.daytimeSign = daytimeSign;
        this.normTime = NORM_TIME;

        workTime = new double[DAY_OF_MONTH];
        for(int i = 0; i < DAY_OF_MONTH; ++i) workTime[i] = UNINITIALIZED_VALUE;

        workTimeSign = new String[DAY_OF_MONTH];
        for(int i = 0; i < DAY_OF_MONTH; ++i) workTimeSign[i] = "-";
    }


    /*******************************************************************************************************************************************
                                                        getters and setters
     ******************************************************************************************************************************************/


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


    public void setNormTime(final double normTime){ this.normTime = normTime; }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getRule(){ return rule; }
    public int getLengthRule(){ return rule.length(); }
    public double getDaytime(){ return daytime; }
    public double getUniqueTime(){ return getDaytime(); }
    public double getNightTime(){ return getDaytime(); }
    public String getDaytimeSign(){ return daytimeSign; }
    public String getUniqueTimeSign(){ return getDaytimeSign(); }
    public String getNightTimeSign(){ return getDaytimeSign(); }
    public int getCounter(){ return counter; }
    public double getNormTime(){ return normTime; }


    public double getWorkTime(final int indexDay){
        try{
            if(indexDay >= workTime.length) throw new Exception("Попытка выхода за пределы массива");
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        return workTime[indexDay];
    }

   public String getWorkTimeSign(final int indexDay) {
        try {
            if(indexDay >= workTime.length) throw new Exception("Попытка выхода за пределы массива");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(0);
        }
        return workTimeSign[indexDay];
    }

    public char getRuleOfDay(final int indexDay) {
        int rulesPosition = counter + indexDay;
        rulesPosition %= rule.length();
        if(indexDay < 0) rulesPosition = rule.length() - rulesPosition;
        return rule.charAt(rulesPosition);
    }


    public void setWorkTime(final int indexDay, final double time){
        try{
            if(indexDay >= workTime.length) throw new Exception("Попытка выхода за пределы массива");
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        workTime[indexDay] = time;
    }

    /*private boolean checkRuleInput(final String rule){
        int sizeRule = rule.length();
        for(int positionForRule = 0; positionForRule < sizeRule; ++positionForRule){
            switch(rule.charAt(positionForRule)){
                case CHAR_DESIGNATION_UNIVERSAL_DAY: break;
                case CHAR_DESIGNATION_WEEKEND: break;
                case CHAR_DESIGNATION_NIGHT: break;
                case CHAR_DESIGNATION_DAY: break;
                default: return false;
            }
        }
        return true;
    }*/

    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/

    public double getWorkHoursPerMonth(final int AMOUNT_OF_DAYS){
        double sumWorkTime = 0;
        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay) {
            sumWorkTime += getWorkTime(indexDay);
        }
        return sumWorkTime;
    }

    private void setWorkTimeSign(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        final String SECOND_NIGHT_SHIFT_FOR_HOLIDAYS = "C_33";
        for(int indexDay = 0; indexDay < workTime.length; ++indexDay){
            double hour = getWorkTime(indexDay);
            Integer codeDay = shortDayAndHolidays.get(indexDay + 1);
            if(codeDay != null && codeDay == Graph.CODE_SHORT_DAY){
                if(getWorkTime(indexDay) != 0) ++hour;
            }
            boolean isStandardOrUniqueGraph = this instanceof GraphStandard || this instanceof GraphUnique;
            if(isStandardOrUniqueGraph && getRuleOfDay(indexDay) != Graph.CHAR_DESIGNATION_WEEKEND) {
                if (getRuleOfDay(indexDay) == 'd') hour = getDaytime();
                else hour = getUniqueTime();
            }
            String hourName;
            if(getRuleOfDay(indexDay) == Graph.CHAR_DESIGNATION_DAY){
                if(hour == getDaytime()) hourName = getDaytimeSign();
                else hourName = findHourName(dayHours, hour);
            }
            else if(getRuleOfDay(indexDay) == Graph.CHAR_DESIGNATION_NIGHT){
                if(hour == getNightTime()){
                    hourName = getNightTimeSign();
                    if(codeDay != null && codeDay == Graph.CODE_HOLIDAY){
                        int positionForRulePreviousDay = indexDay - 1;
                        if(positionForRulePreviousDay < 0) positionForRulePreviousDay = indexDay - 1;
                        if(getRuleOfDay(positionForRulePreviousDay) == Graph.CHAR_DESIGNATION_NIGHT){
                            hourName = SECOND_NIGHT_SHIFT_FOR_HOLIDAYS;
                        }
                    }
                }
                else hourName = findHourName(nightHours, hour);
            }
            else{
                if(hour == getUniqueTime()) hourName = getUniqueTimeSign();
                else hourName = findHourName(dayHours, hour);
            }
            workTimeSign[indexDay] = hourName;
        }
    }

    private static String findHourName(Map<Double, String> hours, double desiredValue){
        String hourName = hours.get(desiredValue);
        if(hourName == null){
            System.out.println("Нет графика на: " + desiredValue + " час");
            hourName = "???";
        }
        return hourName;
    }
// *********************************************************************************************************************
    public void startGenerating(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        setWeekend();
        setShortDaysAndHolidays(shortDayAndHolidays);
        generateGraph();
        setWorkTimeSign(shortDayAndHolidays, dayHours, nightHours);
    }

    private void setWeekend(){
        for(int indexDay = 0; indexDay < workTime.length; ++indexDay){
            if(getRuleOfDay(indexDay) == CHAR_DESIGNATION_WEEKEND){
                workTime[indexDay] = 0;
            }
        }
    }

    protected void setShortDaysAndHolidays(final Map<Integer, Integer> shortDayAndHolidays){
        for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()) {
            if(getRuleOfDay(obj.getKey() - 1) != CHAR_DESIGNATION_WEEKEND) {
                if(obj.getValue() == CODE_SHORT_DAY) workTime[obj.getKey() - 1] = getDaytime() - 1;
                else if(obj.getValue() == CODE_HOLIDAY) workTime[obj.getKey() - 1] = getDaytime();
            }
        }
    }

    protected int calcMissingDays(){
        int amountMissingDays = 0;
        for(int indexDay = 0; indexDay < workTime.length; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE) ++amountMissingDays;
        }
        return amountMissingDays;
    }

    protected double calcMissingTime(){
        double missingTime = 0;
        for(int indexDay = 0; indexDay < workTime.length; ++indexDay){
            if(getWorkTime(indexDay) != UNINITIALIZED_VALUE) missingTime += getWorkTime(indexDay);
        }
        return getNormTime() - missingTime;
    }

    protected int calcDaysWithMinTime(final int minWorkTime, final int maxWorkTime, final double sumWorkTime, final int amountWorkDays){
        int daysWithMinTime;
        for(daysWithMinTime = 0; daysWithMinTime <= amountWorkDays; ++daysWithMinTime){
            int daysWithMaxTime = amountWorkDays - daysWithMinTime;
            if(daysWithMinTime * minWorkTime + daysWithMaxTime * maxWorkTime - sumWorkTime < ACCEPTABLE_ACCURACY) break;
        }
        return daysWithMinTime;
    }

    protected double calcFrequency(final int amountDaysWithMinTime, final int amountDaysWithMaxTime){
        if(amountDaysWithMinTime > amountDaysWithMaxTime)
            return amountDaysWithMaxTime == 0 ? amountDaysWithMinTime : (double) amountDaysWithMinTime / amountDaysWithMaxTime;
        return amountDaysWithMinTime == 0 ? amountDaysWithMaxTime : (double) amountDaysWithMaxTime / amountDaysWithMinTime;
    }

    private void fillMissingWorkDays(final int spreadValue, final int amountSpreadValue, final int rareValue,
                                      final int amountRareValue, final double frequency){
        double currentFrequency = 0;
        int counterSpreadTime = 0, counterRareTime = 0;

        for(int indexDay = 0; indexDay < workTime.length; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(currentFrequency < frequency && counterSpreadTime < amountSpreadValue || counterRareTime == amountRareValue){
                    workTime[indexDay] = spreadValue;
                    ++counterSpreadTime;
                    ++currentFrequency;
                }else{
                    workTime[indexDay] = rareValue;
                    ++counterRareTime;
                    currentFrequency -= frequency;
                }
            }
        }
    }

    protected void generateGraph(){
        int amountMissingDays = calcMissingDays();
        double missingTime = calcMissingTime();

        int minWorkTime = amountMissingDays == 0 ? (int) missingTime : (int) missingTime / amountMissingDays;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = calcDaysWithMinTime(minWorkTime, maxWorkTime, missingTime, amountMissingDays);
        int amountDaysWithMaxTime = amountMissingDays - amountDaysWithMinTime;
        double frequency = calcFrequency(amountDaysWithMinTime, amountDaysWithMaxTime);

        int spreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountSpreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;
        int rareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountRareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;
        fillMissingWorkDays(spreadValue, amountSpreadValue, rareValue, amountRareValue, frequency);
    }
}