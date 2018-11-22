package com.example.barbara.skytonight.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public abstract class AstroEvent implements Serializable {
    private int id;
    private String name;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar peakDate;
    private double latitude;
    private double longitude;

    public AstroEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peakDate = peakDate;
    }

    public AstroEvent(int id, String name, Calendar startDate, Calendar endDate, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peakDate = startDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AstroEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peakDate = peakDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public Calendar getPeak() { return peakDate; }

    public Date getPeakDate() { return peakDate.getTime(); }

    public Date getStartDate() { return startDate.getTime(); }

    public Date getEndDate() { return endDate.getTime(); }

    public String getName() { return name; }

    public String getLongName() { return name; }

    public int getId() {
        return id;
    }

}
