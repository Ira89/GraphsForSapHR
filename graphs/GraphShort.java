package ru.polynkina.irina.graphs;

public class GraphShort extends Graph {

    public GraphShort(int id, String name, String rule, double daytime, String daytimeSign){
        super(id, name, rule, daytime, daytimeSign);
    }

    @Override
    protected void setNormTime(double normTime) {
        super.setNormTime((int) normTime / 2);
    }
}
