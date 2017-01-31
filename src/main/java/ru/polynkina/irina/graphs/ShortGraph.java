package ru.polynkina.irina.graphs;

public class ShortGraph extends DayGraph {

    public ShortGraph(int id, String name, String rule, double basicTime, String basicTimeSign, String text) throws Exception{
        super(id, name, rule, basicTime, basicTimeSign, text);
    }

    // ----------------------------------------------- step 2 ----------------------------------------------------------
    // Норма у данного типа графиков отличается от обычной месячной нормы (и это нормально)
    // Рассчитываем ее, исходя из нормы обычных 8-часовых графиков (с учетом сокращенных дней) и перезаписываем
    //
    // К примеру, норма в месяце для стандартных графиков равна 166 часам
    // Пока 166 не делится на 8 без остатка, увеличиваем норму времени (нужно увеличить на 2 часа, тогда получим 168 % 8 == 0)
    // Пусть основное время для графика типа ShortGraph равно 4 часам
    // Норма для ShortGraph должна составить 82 часа (168 / 8 * 4 часа - 2 часа, на которые мы увеличивали)
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
