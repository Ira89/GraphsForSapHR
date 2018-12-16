package ru.polynkina.irina.beans;

public class MinMaxNormTime {

    private int minNormTime;
    private int maxNormTime;

    public MinMaxNormTime(int minNormTime, int maxNormTime) {
        this.minNormTime = minNormTime;
        this.maxNormTime = maxNormTime;
    }

    public int getMinNormTime() {
        return minNormTime;
    }

    public int getMaxNormTime() {
        return maxNormTime;
    }
}
