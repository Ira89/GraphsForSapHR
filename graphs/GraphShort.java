package ru.polynkina.irina.graphs;

public class GraphShort extends Graph {

    public GraphShort(int id, String name, String rule, double daytime, String daytimeSign){
        super(id, name, rule, daytime, daytimeSign);
    }

    @Override
    protected void setNormTime(double normTime){
        final double STANDARD_TIME_IN_DAY = 8.0;
        int hoursInShortDays = 0;
        while(normTime % STANDARD_TIME_IN_DAY != 0){
            ++normTime;
            ++hoursInShortDays;
        }
        double amountDay = normTime / STANDARD_TIME_IN_DAY;
        double newWorkTimeInMonth =  getDaytime() * amountDay - hoursInShortDays;
        super.setNormTime(newWorkTimeInMonth);
    }
}
