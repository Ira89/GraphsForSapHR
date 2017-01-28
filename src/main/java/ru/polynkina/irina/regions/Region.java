package ru.polynkina.irina.regions;

public interface Region {

    String getNameRegion();
    boolean generationNeededForRegion();
    boolean graphIsUsedInRegion(String nameGraph);

}
