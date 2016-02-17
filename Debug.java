package ru.polynkina.irina.graphs;

import java.util.List;
import java.util.Map;

public class Debug {

    public static void printDayMonthAndYear(final int AMOUNT_OF_DAYS, final int MONTH, final int YEAR){
        System.out.println("amountDay/month/year: " + AMOUNT_OF_DAYS + "/" + MONTH + "/" + YEAR);
    }

    public static void printNormTime(final double NORM_TIME){
        System.out.println("normTime: " + NORM_TIME);
    }

    public static void printShortDayAndHoliday(final Map<Integer, Integer> shortDayAndHolidays){
        for(Map.Entry<Integer, Integer> obj : shortDayAndHolidays.entrySet()) {
            System.out.println("day: " + obj.getKey() + " \tcode: " + obj.getValue());
        }
    }

    public static void printInfoAboutGraphs(final List<Graph> graphs){
        for(Graph obj : graphs) {
            obj.printInfo();
        }
    }

    public static void printInfoAboutWorkTime(final List<Graph> graphs, final int AMOUNT_OF_DAYS){
        for(Graph obj : graphs){
            obj.printWorkTime(AMOUNT_OF_DAYS);
        }
    }

    public static void printHoursName(final Map<Double, String> hoursName){
        for(Map.Entry<Double, String> obj : hoursName.entrySet()){
            System.out.println("hourName: " + obj.getValue() + "\thour: " + obj.getKey());
        }
    }
}