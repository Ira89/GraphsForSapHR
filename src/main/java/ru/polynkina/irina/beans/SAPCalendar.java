package ru.polynkina.irina.beans;

import java.util.List;

public class SAPCalendar {

    private List<String> calendars;

    public SAPCalendar(List<String> calendars) {
        this.calendars = calendars;
    }

    public List<String> getCalendars() {
        return calendars;
    }

    public String getCalendarByIndex(int index) {
        return calendars.get(index);
    }
}
