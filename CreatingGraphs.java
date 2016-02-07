package ru.polynkina.irina.graphs;

import java.util.HashMap;
import java.util.Map;


public class CreatingGraphs {

    final static int MAX_DEBUG_LEVEL = 4;
    final static int DEBUG_USER_DATA = 3;
    final static char ACTIVATED_DEBUG = '1';



    public static void main(String[] debugLevel) {
        System.out.println("Начинаю генерацию графиков!");

        int yearAndMonth = UserInteraction.readYearAndMonth();
        final int YEAR = yearAndMonth >> UserInteraction.BITS_IN_MONTH;
        final int MONTH = yearAndMonth & UserInteraction.MASK_FOR_MONTH;
        final int AMOUNT_DAY = Calendar.getAmountDay(MONTH, YEAR);
        final int NORM_TIME = UserInteraction.readNormTime();

        Map<Integer, Integer> holidays = new HashMap<Integer, Integer>();
        holidays = UserInteraction.readHolidays(holidays);

        if(MAX_DEBUG_LEVEL == debugLevel[0].length()){
            String commandLine = debugLevel[0];
            if(commandLine.charAt(DEBUG_USER_DATA) == ACTIVATED_DEBUG){
                System.out.println("AMOUNT_DAY/MONTH/YEAR: " + AMOUNT_DAY + "/" + MONTH + "/" + YEAR);
                System.out.println("NORM_TIME: " + NORM_TIME);
                for(Map.Entry<Integer, Integer> map : holidays.entrySet()){
                    System.out.println("DAY: " + map.getKey() + " CODE: " + map.getValue());
                }
            }
        }

        System.out.println("Генерация графиков завершена!");
    }
}