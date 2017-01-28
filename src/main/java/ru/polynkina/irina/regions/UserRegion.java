package ru.polynkina.irina.regions;

import java.util.*;

class UserRegion implements Region {

    private String nameRegion;
    private boolean needToGenerate;
    private List<String> nameGraphs = new ArrayList<String>();

    protected UserRegion(String nameRegion, boolean needToGenerate, List<String> nameGraphs) {
        this.nameRegion = nameRegion;
        this.needToGenerate = needToGenerate;
        this.nameGraphs.addAll(nameGraphs);
    }

    public String getNameRegion() { return nameRegion; }
    public boolean generationNeededForRegion() { return needToGenerate; }
    public boolean graphIsUsedInRegion(String nameGraph) { return nameGraphs.contains(nameGraph); }
}
