package ru.polynkina.irina.graphs;

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

        if(MAX_DEBUG_LEVEL == debugLevel[0].length() ){
            String commandLine = debugLevel[0];
            if(commandLine.charAt(DEBUG_USER_DATA) == ACTIVATED_DEBUG){
                System.out.println("MONTH/AMOUNT_DAY/YEAR: " + MONTH + "/" + AMOUNT_DAY + "/" + YEAR);
            }
        }

        System.out.println("Генерация графиков завершена!");
    }
}