package com.example.barbara.skytonight.entity;

import java.util.Calendar;
import java.util.Date;

public class MeteorShowerEvent extends AstroEvent {

    private Calendar peakDate;
    private String showerLongName;

    public MeteorShowerEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate) {
        super(id, "ms_" + name.toLowerCase().replace(' ', '_'), startDate, endDate);
        this.peakDate = peakDate;
        this.showerLongName = name + " Meteor Shower";
    }

    @Override
    public Date getPeakDate() {
        return peakDate.getTime();
    }

    @Override
    public String getLongName() { return showerLongName; }
}
