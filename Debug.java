package ru.polynkina.irina.graphs;

import java.util.List;
import java.util.Map;

public class Debug {

    public static void printDayMonthAndYear(int amountDay, int month, int year){
        System.out.println("amountDay/month/year: " + amountDay + "/" + month + "/" + year);
    }

    public static void printNormTime(double normTime){
        System.out.println("normTime: " + normTime);
    }

    public static void printShortDayAndHoliday(Map<Integer, Integer> shortDayAndHolidays){
        for(Map.Entry<Integer, Integer> map : shortDayAndHolidays.entrySet()) {
            System.out.println("day: " + map.getKey() + " code: " + map.getValue());
        }
    }

    public static void printInfoAboutGraphs(List<Graph> graphs){
        for(Graph obj : graphs) obj.printInfo();
    }

    public static void printInfoAboutWorkTime(List<Graph> graphs, int amountDay){
        for(Graph obj : graphs) obj.printWorkTime(amountDay);
    }
}
