package com.example.barbara.skytonight.data;

import java.util.Calendar;
import java.util.Date;

public class AstroEvent {
    private int id;
    private String name;
    private Calendar startDate;
    private Calendar endDate;

    public AstroEvent() {
        this.id = -1;
        this.name = "Empty";
    }

    public AstroEvent(int id, String name, Calendar startDate, Calendar endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() { return startDate.getTime(); }

    public Date getEndDate() { return endDate.getTime(); }

    public String getName() { return name; }

    public int getId() {
        return id;
    }

}
