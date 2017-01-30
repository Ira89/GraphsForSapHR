package ru.polynkina.irina.graphs;

public class ShortGraph extends DayGraph {

    public ShortGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception{
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    // ----------------------------------------------- step 2 ----------------------------------------------------------
    // Норма у данного типа графиков отличается от обычной месячной нормы (и это нормально)
    // Рассчитываем ее, исходя из нормы обычных 8-часовых графиков (с учетом сокращенных дней) и перезаписываем
    @Override
    protected void setNormTime(double normTime) {
        int reducedHours = 0;
        while(normTime % STANDARD_TIME_IN_DAY != 0) {
            ++normTime;
            ++reducedHours;
        }
        double amountDay = normTime / STANDARD_TIME_IN_DAY;
        double newNormTime =  getBasicTime() * amountDay - reducedHours;
        super.setNormTime(newNormTime);
    }
}
