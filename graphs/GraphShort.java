package ru.polynkina.irina.graphs;

public class GraphShort extends Graph {

    public GraphShort(int id, String name, String rule, double daytime, String daytimeSign, final double NORM_TIME, final int DAY_OF_MONTH){
        super(id, name, rule, daytime, daytimeSign, (int) NORM_TIME * 0.5, DAY_OF_MONTH);
    }
}
