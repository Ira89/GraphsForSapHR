package ru.polynkina.irina.graphs;

import java.util.Map;
import ru.polynkina.irina.main.*;

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
    private double workTimeInMonth;
    private double workTime[];
    private String workTimeSign[];


    public Graph(int id, String name, String rule, double daytime, String daytimeSign, final double workTimeInMonth) {
        this.id = id;
        this.name = name;
        this.rule = rule;
        this.daytime = daytime;
        this.daytimeSign = daytimeSign;
        this.workTimeInMonth = workTimeInMonth;
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


    public void setWorkTimeInMonth(final double workTimeInMonth){ this.workTimeInMonth = workTimeInMonth; }
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
    public double getWorkTimeInMonth(){ return workTimeInMonth; }


    public double getWorkTime(final int indexDay){
        try{
            if(indexDay >= workTime.length) throw new Exception("Попытка выхода за пределы массива");
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        return workTime[indexDay];
    }

    public char getRuleOfDay(final int positionForRule){
        try{
            if(positionForRule >= getLengthRule()) throw new Exception("Попытка выхода за пределы правила");
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }
        return rule.charAt(positionForRule);
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

    /*******************************************************************************************************************************************
                                                        private methods
     ******************************************************************************************************************************************/


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



    private void fillUninitializedWorkDays(final int spreadValue, final int amountSpreadValue, final int rareValue,
                                           final int amountRareValue, final double frequency){
        double currentFrequency = 0;
        int counterSpreadTime = 0;
        int counterRareTime = 0;

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(currentFrequency < frequency && counterSpreadTime < amountSpreadValue || counterRareTime == amountRareValue){
                    setWorkTime(indexDay, spreadValue);
                    ++counterSpreadTime;
                    ++currentFrequency;
                }else{
                    setWorkTime(indexDay, rareValue);
                    ++counterRareTime;
                    currentFrequency = 0;
                }
            }
        }
    }


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    protected int getAmountDaysWithMinTime(final int minWorkTime, final int maxWorkTime, final double sumWorkTime, final int amountWorkDays){
        int daysWithMinTime;
        for(daysWithMinTime = 0; daysWithMinTime <= amountWorkDays; ++daysWithMinTime){
            int daysWithMaxTime = amountWorkDays - daysWithMinTime;
            if(daysWithMinTime * minWorkTime + daysWithMaxTime * maxWorkTime - sumWorkTime < ACCEPTABLE_ACCURACY) break;
        }
        return daysWithMinTime;
    }



    protected double calculateFrequency(final int amountDaysWithMinTime, final int amountDaysWithMaxTime){
        double frequency;
        if(amountDaysWithMinTime > amountDaysWithMaxTime){
            if(amountDaysWithMaxTime != 0) frequency = (double) amountDaysWithMinTime / amountDaysWithMaxTime;
            else frequency = amountDaysWithMinTime;
        } else{
            if(amountDaysWithMinTime != 0) frequency = (double) amountDaysWithMaxTime / amountDaysWithMinTime;
            else frequency = amountDaysWithMaxTime;
        }
        return frequency;
    }



    private void createUninitializedWorkTimeArray(){
        workTime = new double[Main.DAY_OF_MONTH];
        workTimeSign = new String[Main.DAY_OF_MONTH];
        for(int i = 0; i < Main.DAY_OF_MONTH; ++i){
            setWorkTime(i, UNINITIALIZED_VALUE);
        }
    }



    private void setWeekend(){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_WEEKEND){
                setWorkTime(indexDay, 0);
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }



    protected void setShortDayAndHolidays(final Map<Integer, Integer> shortDayAndHolidays){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getRuleOfDay(positionForRule) != CHAR_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()){
                    if(obj.getKey() == indexDay + 1){
                        if(obj.getValue() == CODE_SHORT_DAY) setWorkTime(indexDay, getDaytime() - 1);
                        else if(obj.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, getDaytime());
                    }
                }
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }



    protected int getAmountUninitializedDays(){
        int amountUninitializedDays = 0;
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE) ++amountUninitializedDays;
        }
        return amountUninitializedDays;
    }



    protected double getSumTimeInitializedDays(){
        double sumTime = 0;
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            if(getWorkTime(indexDay) != UNINITIALIZED_VALUE) sumTime += getWorkTime(indexDay);
        }
        return sumTime;
    }



    protected void generateGraph(final int amountUninitializedDays, final double sumTimesUninitializedDays){
        double averageWorkTime = sumTimesUninitializedDays;
        if(amountUninitializedDays != 0) averageWorkTime /= amountUninitializedDays;

        int minWorkTime = (int) averageWorkTime;
        int maxWorkTime = minWorkTime + 1;

        int amountDaysWithMinTime = getAmountDaysWithMinTime(minWorkTime, maxWorkTime, sumTimesUninitializedDays, amountUninitializedDays);
        int amountDaysWithMaxTime = amountUninitializedDays - amountDaysWithMinTime;
        double frequency = calculateFrequency(amountDaysWithMinTime, amountDaysWithMaxTime);

        int spreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountSpreadValue = amountDaysWithMinTime >= amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;
        int rareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? minWorkTime : maxWorkTime;
        int amountRareValue = amountDaysWithMinTime < amountDaysWithMaxTime ? amountDaysWithMinTime : amountDaysWithMaxTime;
        fillUninitializedWorkDays(spreadValue, amountSpreadValue, rareValue, amountRareValue, frequency);
    }



    public double getWorkHoursPerMonth(final int AMOUNT_OF_DAYS){
        double sumWorkTime = 0;
        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay) {
            sumWorkTime += getWorkTime(indexDay);
        }
        return sumWorkTime;
    }

    private void setWorkTimeSign(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        final String SECOND_NIGHT_SHIFT_FOR_HOLIDAYS = "C_33";
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();
        for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
            double hour = getWorkTime(indexDay);
            Integer codeDay = shortDayAndHolidays.get(indexDay + 1);
            if(codeDay != null && codeDay == Graph.CODE_SHORT_DAY){
                if(getWorkTime(indexDay) != 0) ++hour;
            }
            String hourName;
            if(getRuleOfDay(positionForRule) == Graph.CHAR_DESIGNATION_DAY){
                if(hour == getDaytime()) hourName = getDaytimeSign();
                else hourName = findHourName(dayHours, hour);
            }
            else if(getRuleOfDay(positionForRule) == Graph.CHAR_DESIGNATION_NIGHT){
                if(hour == getNightTime()){
                    hourName = getNightTimeSign();
                    if(codeDay != null && codeDay == Graph.CODE_HOLIDAY){
                        int positionForRulePreviousDay = positionForRule - 1;
                        if(positionForRulePreviousDay < 0) positionForRulePreviousDay = lengthRule - 1;
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
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }

    private static String findHourName(Map<Double, String> hours, double desiredValue){
        String hourName = hours.get(desiredValue);
        if(hourName == null){
            System.out.println("Не могу найти график на: " + desiredValue + " час");
            hourName = "FREE";
        }
        return hourName;
    }

    public void startGenerating(Map<Integer, Integer> shortDayAndHolidays, Map<Double, String> dayHours, Map<Double, String> nightHours) {
        createUninitializedWorkTimeArray();
        setWeekend();
        setShortDayAndHolidays(shortDayAndHolidays);
        generateGraph(getAmountUninitializedDays(), workTimeInMonth - getSumTimeInitializedDays());
        setWorkTimeSign(shortDayAndHolidays, dayHours, nightHours);
    }
}