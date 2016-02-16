package ru.polynkina.irina.graphs;

public class GraphDiurnal extends Graph {

    final static int INDEX_MIDDLE_DAY_OFF = 2;
    final static double MAX_WORK_TIME_IN_DAY = 22;

    GraphDiurnal(int id, String name, String rule, double daytime, String daytimeSign, double workTimeInMonth){
        super(id, name, rule, daytime, daytimeSign, workTimeInMonth);
    }


    private void setAdditionalWorkDay(double additionalTime, int amountDay){
        int lengthRule = getLengthRule();
        int currentCounter = getCounter();
        for(int indexDay = 0; indexDay < amountDay && additionalTime != 0; ++indexDay){
            if(getWorkTime(indexDay) == CHAR_DESIGNATION_NIGHT && indexDay + INDEX_MIDDLE_DAY_OFF < amountDay){
                double additionalHours = additionalTime < MAX_WORK_TIME_IN_DAY ? additionalTime : MAX_WORK_TIME_IN_DAY;
                setWorkTime(indexDay, additionalHours);
                additionalTime -= additionalHours;
            }
            if(++currentCounter == lengthRule) currentCounter = 0;
        }
    }


    public void generateGraph(int amountDay, int amountUninitializedDays, double sumTimesUninitializedDays){
        double averageWorkTime;
        if(amountDay != 0) averageWorkTime = sumTimesUninitializedDays / amountDay;
        else averageWorkTime = sumTimesUninitializedDays;
        if(averageWorkTime > MAX_WORK_TIME_IN_DAY){
            double additionalTime = sumTimesUninitializedDays - (amountUninitializedDays * MAX_WORK_TIME_IN_DAY);
            setAdditionalWorkDay(additionalTime, amountDay);
            sumTimesUninitializedDays -= additionalTime;
        }
        super.generateGraph(amountDay, amountUninitializedDays, sumTimesUninitializedDays);
    }
}
