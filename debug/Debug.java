package ru.polynkina.irina.debug;

import ru.polynkina.irina.graphs.Graph;
import ru.polynkina.irina.fileInteraction.FileInteraction;
import ru.polynkina.irina.main.Main;

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
        System.out.println(Main.DAY_OF_MONTH + "/" + Main.MONTH + "/" + Main.YEAR);
    }


    private static void printNormTime(){ System.out.println("normTime: " + Main.NORM_TIME); }


    private static void printShortDayAndHoliday(final Map<Integer, Integer> shortDayAndHolidays){
        for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet())
            System.out.println("day: " + obj.getKey() + " \tcode: " + obj.getValue());
    }


    private static void printInfoAboutGraphs(final List<Graph> graphs){
        for(Graph obj : graphs) {
            System.out.print("id: " + obj.getId() + "\tname: " + obj.getName() + "\trule: " + obj.getRule());
            System.out.print("\tdaytime: " + obj.getDaytime() + "\tdaytimeSign: " + obj.getDaytimeSign());
            System.out.print("\tnightTime: " + obj.getNightTime() + "\tnightTimeSign: " + obj.getNightTimeSign());
            System.out.println("\tworkTimeInMonth: " + obj.getNormTime() + "\tcounter: " + obj.getCounter());
        }
    }


    private static void printInfoAboutWorkTime(final List<Graph> graphs){
        for(Graph obj : graphs) {
            System.out.print(obj.getName() + ": \t");
            for(int indexDay = 0; indexDay < Main.DAY_OF_MONTH; ++indexDay){
                System.out.print(obj.getWorkTime(indexDay) + " ");
            }
            System.out.println();
        }
    }


    private static void printHoursName(final Map<Double, String> hoursName){
        for(Map.Entry<Double, String> obj : hoursName.entrySet())
            System.out.println("hourName: " + obj.getValue() + "\thour: " + obj.getKey());
    }


    public static void start(final String commandLine, final Map<Integer, Integer> shortDayAndHolidays, final List<Graph> graphs,
                       final Map<Double, String> dayHours, final Map<Double, String> nightHours) {
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
                FileInteraction.writeWorkTimeInFile(graphs, Main.DAY_OF_MONTH);
            }
            if(commandLine.charAt(DEBUG_READ_HOURS) == ACTIVATED_DEBUG){
                printHoursName(dayHours);
                printHoursName(nightHours);
            }
        }
    }
}