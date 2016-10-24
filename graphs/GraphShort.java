package ru.polynkina.irina.graphs;

public class GraphShort extends Graph {

    public GraphShort(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, (int) workTimeInMonth * 0.5);
    }
}
