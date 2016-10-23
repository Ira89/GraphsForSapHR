package ru.polynkina.irina.graphs;

import java.util.List;
import java.util.Map;

public class Debug {

    private static final int MAX_DEBUG_LEVEL = 5;
    private static final int DEBUG_USER_INPUT = 0;
    private static final int DEBUG_READ_GRAPHS = 1;
    private static final int DEBUG_GENERATION_TIME = 2;
    private static final int WRITTEN_WORK_TIME = 3;
    private static final int DEBUG_READ_HOURS = 4;
    private static final char ACTIVATED_DEBUG = '1';

    private static void printDayMonthAndYear(){
        System.out.print("amountDay/month/year: ");
        System.out.println(CreatingGraphs.AMOUNT_OF_DAYS + "/" + CreatingGraphs.MONTH + "/" + CreatingGraphs.YEAR);
    }

    private static void printNormTime(){
        System.out.println("normTime: " + CreatingGraphs.NORM_TIME);
    }

    private static void printShortDayAndHoliday(final Map<Integer, Integer> shortDayAndHolidays){
        for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet())
            System.out.println("day: " + obj.getKey() + " \tcode: " + obj.getValue());
    }

    private static void printInfoAboutGraphs(final List<Graph> graphs){
        for(Graph obj : graphs) obj.printInfo();
    }

    private static void printInfoAboutWorkTime(final List<Graph> graphs){
        for(Graph obj : graphs) obj.printWorkTime();
    }

    private static void printHoursName(final Map<Double, String> hoursName){
        for(Map.Entry<Double, String> obj : hoursName.entrySet())
            System.out.println("hourName: " + obj.getValue() + "\thour: " + obj.getKey());
    }

    public static void start(final String commandLine, final Map<Integer, Integer> shortDayAndHolidays,
                             final List<Graph> graphs, final Map<Double, String> dayHours, final Map<Double, String> nightHours) {
        if(MAX_DEBUG_LEVEL == commandLine.length()){
            if(commandLine.charAt(DEBUG_USER_INPUT) == ACTIVATED_DEBUG){
                printDayMonthAndYear();
                printNormTime();
                printShortDayAndHoliday(shortDayAndHolidays);
            }
            if(commandLine.charAt(DEBUG_READ_GRAPHS) == ACTIVATED_DEBUG){
                printInfoAboutGraphs(graphs);
            }
            if(commandLine.charAt(DEBUG_GENERATION_TIME) == ACTIVATED_DEBUG){
                printInfoAboutWorkTime(graphs);
            }
            if(commandLine.charAt(WRITTEN_WORK_TIME) == ACTIVATED_DEBUG){
                FileInteraction.writeWorkTimeInFile(graphs, CreatingGraphs.AMOUNT_OF_DAYS);
            }
            if(commandLine.charAt(DEBUG_READ_HOURS) == ACTIVATED_DEBUG){
                printHoursName(dayHours);
                printHoursName(nightHours);
            }
        }
    }
}