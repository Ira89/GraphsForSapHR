package ru.polynkina.irina.graphs;

public class GraphShort extends Graph {

    final static double FRACTION_OF_TIME_FOR_GRAPHS_SHORT = 0.5;


    GraphShort(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, (int) workTimeInMonth * FRACTION_OF_TIME_FOR_GRAPHS_SHORT);
    }
}
