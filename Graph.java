package ru.polynkina.irina.graphs;

public class Graph {

    private int id;
    private String name;
    private String rule;
    private double daytime;
    private String daytimeSign;
    private int counter;
    private double workTime[];

    Graph(int id, String name, String rule, double daytime, String daytimeSign){
        this.id = id;
        this.name = name;
        this.rule = rule;
        this.daytime = daytime;
        this.daytimeSign = daytimeSign;
    }

    public void printInfo(){
        System.out.print("id: " + id + "\tname: " + name + "\trule: " + rule);
        System.out.println("\tdaytime: " + daytime + "\tdaytimeSign: " + daytimeSign + "\tcounter: " + counter);
    }

    public void setCounter(int counter){
        this.counter = counter;
    }

    public int getId(){
        return id;
    }
}