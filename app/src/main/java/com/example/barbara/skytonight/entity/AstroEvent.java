package com.example.barbara.skytonight.entity;

import java.util.Calendar;
import java.util.Date;

public abstract class AstroEvent {
    private int id;
    private String name;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar peakDate;

    public AstroEvent() {
        this.id = -1;
        this.name = "";
    }

    public AstroEvent(int id, String name, Calendar startDate, Calendar endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peakDate = startDate;
    }

    public AstroEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peakDate = peakDate;
    }

    public Date getPeakDate() { return peakDate.getTime(); }

    public Date getStartDate() { return startDate.getTime(); }

    public Date getEndDate() { return endDate.getTime(); }

    public String getName() { return name; }

    public String getLongName() { return name; }

    public int getId() {
        return id;
    }

}
