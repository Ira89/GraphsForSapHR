package ru.polynkina.irina.graphs;

import java.util.Map;

public class GraphUnique extends Graph {

    private double uniqueTime;
    private String uniqueTimeSign;


    GraphUnique(int id, String name, String rule, double daytime, String daytimeSign,
                double uniqueTime, String uniqueTimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);

        try{
            boolean isCorrectTime = checkTimeInput(uniqueTime);
            if(!isCorrectTime) throw new Exception("Время не может принимать значение: " + uniqueTime);
        }catch (Exception exc){
            exc.printStackTrace();
            System.exit(0);
        }

        this.uniqueTime = uniqueTime;
        this.uniqueTimeSign = uniqueTimeSign;
    }



    public void printInfo(){
        System.out.print("id: " + getId() + "\tname: " + getName() + "\trule: " + getRule() + "\tdaytime: " + getDaytime());
        System.out.print("\tdaytimeSign: " + getDaytimeSign() + "\tuniqueTime: " + uniqueTime + "\tuniqueTimeSign: " + uniqueTimeSign);
        System.out.println("\tworkTimeInMonth: " + getWorkTimeInMonth() + "\tcounter: " + getCounter());
    }


    /*******************************************************************************************************************************************
                                                        getters and setters
     ******************************************************************************************************************************************/


    public double getUniqueTime(){
        return uniqueTime;
    }


    public String getUniqueTimeSign(){
        return uniqueTimeSign;
    }


    /*******************************************************************************************************************************************
                                                        public methods
     ******************************************************************************************************************************************/


    public void setShortDayAndHolidays(final Map<Integer, Integer> shortDayAndHolidays, final int AMOUNT_OF_DAYS){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
            if(getRuleOfDay(positionForRule) != CHAR_DESIGNATION_WEEKEND){
                for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()){
                    if(obj.getKey() == indexDay + 1){
                        if(obj.getValue() == CODE_SHORT_DAY){
                            if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime() - 1);
                            else if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime() - 1);
                        }
                        else if(obj.getValue() == CODE_HOLIDAY) setWorkTime(indexDay, 0);
                        else if(obj.getValue() == CODE_DAY_OFF) setWorkTime(indexDay, 0);
                    }
                }
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }



    public void generateGraph(final int amountUninitializedDays, final double sumTimesUninitializedDays, final int AMOUNT_OF_DAYS){
        int lengthRule = getLengthRule();
        int positionForRule = getCounter();

        for(int indexDay = 0; indexDay < AMOUNT_OF_DAYS; ++indexDay){
            if(getWorkTime(indexDay) == UNINITIALIZED_VALUE){
                if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_DAY) setWorkTime(indexDay, getDaytime());
                else if(getRuleOfDay(positionForRule) == CHAR_DESIGNATION_UNIVERSAL_DAY) setWorkTime(indexDay, getUniqueTime());
            }
            if(++positionForRule == lengthRule) positionForRule = 0;
        }
    }
}
