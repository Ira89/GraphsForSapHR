package ru.polynkina.irina.graphs;

import ru.polynkina.irina.hours.Hours;
import ru.polynkina.irina.period.ReportingPeriod;

public interface Graph {

    double MAX_WORK_TIME_IN_DIURNAL = 22.0;
    double MAX_WORK_TIME_IN_DAY_TIME = 15.0;
    double STANDARD_TIME_IN_DAY = 8.0;
    double UNINITIALIZED_WORK_TIME = -1.0;

    int CODE_SHORT_DAY = 0;
    int CODE_HOLIDAY = 1;
    int CODE_DAY_OFF = 2;

    String SECOND_NIGHT_SHIFT = "C_33";

    String DAY_TYPE = "DAY";
    String SHORT_TYPE = "SHORT";
    String FIVE_DAY_TYPE = "STANDARD";
    String FRACTIONAL_TYPE = "FLOAT";
    String DIURNAL_TYPE = "DIURNAL";
    String UNIQUE_TYPE = "UNIQUE";
    String MIXED_TYPE = "MIX";
    String SMALL_TYPE = "SMALL";

    char UNIVERSAL_DAY = 'u';
    char WEEKEND = 'f';
    char NIGHT = 'n';
    char DAY = 'd';

    char SIGN_HOLIDAY = '1';
    char SIGN_SHORT_DAY = 'A';
    char SIGN_OFF_DAY = 'F';

    int getId();
    String getName();
    int getLengthRule();
    double getBasicTime();
    String getBasicTimeSign();
    int getCounter();
    double getNormTime();
    String getText();
    double getWorkTime(int indexDay) throws Exception;
    String getWorkTimeSign(int indexDay) throws Exception;
    char getHolidaysSign(int indexDay) throws Exception;
    char getShortDaysSign(int indexDay) throws Exception;

    char getRuleOfDay(int indexDay);
    double calcRealNormTime() throws Exception;
    void startGenerating(ReportingPeriod period, Hours libHours) throws Exception;

    void setCounter(int counter) throws Exception;
    void setWorkTime(int indexDay, double time) throws Exception;
    void setWorkTimeSign(int indexDay, String sign) throws Exception;
    void setHolidaysSign(int indexDay, char sign) throws Exception;
    void setShortDaysSign(int indexDay, char sign) throws Exception;

}
