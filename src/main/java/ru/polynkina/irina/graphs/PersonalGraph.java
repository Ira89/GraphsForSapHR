package ru.polynkina.irina.graphs;

import ru.polynkina.irina.period.ReportingPeriod;
import java.time.DayOfWeek;

public class PersonalGraph extends DayGraph {

    public PersonalGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception {
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    @Override
    protected void setNormTime(ReportingPeriod period) {
        super.setNormTime(period.getNormTime() - period.calculateDayOfWeek(DayOfWeek.FRIDAY));
    }
}
