package com.example.barbara.skytonight.entity;

import java.util.Calendar;
import java.util.Date;

public class MeteorShowerEvent extends AstroEvent {

    private Calendar peakDate;
    private String showerLongName;
    private int zhr;
    private String radiant;

    public MeteorShowerEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate) {
        super(id, "ms_" + name.toLowerCase().replace(' ', '_'), startDate, endDate);
        this.peakDate = peakDate;
        this.showerLongName = name + " Meteor Shower";
    }

    public MeteorShowerEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate, int zhr, String radiant) {
        super(id, "ms_" + name.toLowerCase().replace(' ', '_'), startDate, endDate);
        this.peakDate = peakDate;
        this.showerLongName = name + " Meteor Shower";
        this.zhr = zhr;
        this.radiant = radiant;
    }

    @Override
    public Date getPeakDate() {
        return peakDate.getTime();
    }

    @Override
    public String getLongName() { return showerLongName; }

    public int getZhr() { return zhr; }
}
