package com.example.barbara.skytonight.entity;

import java.util.Calendar;
import java.util.Date;

public class MeteorShowerEvent extends AstroEvent {

    private Calendar peakDate;
    private String showerLongName;
    private int zhr;
    private double rightAsc;
    private double decl;
    private boolean lowVisibility;

    public MeteorShowerEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate, int zhr, double rightAsc, double decl) {
        super(id, "ms_" + name.toLowerCase().replace(' ', '_'), startDate, endDate, 0, 0);
        this.peakDate = peakDate;
        this.showerLongName = name + " Meteor Shower";
        this.zhr = zhr;
        this.rightAsc = rightAsc;
        this.decl = decl;
        this.lowVisibility = false;
    }

    public boolean isVisibilityLow(double latitude, double longitude) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(peakDate.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        int count = 0, altSum = 0, iterations = 10;
        double maxAlt = 0;
        for (int i = 0; i < iterations; i++){
            double alt = calculateAlt(latitude, longitude, calendar);
            if (alt > 10) {
                count++;
            }
            if (alt > maxAlt) {
                maxAlt = alt;
            }
            altSum += alt;
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
        double altAvg = (double) altSum/iterations;
        if (altAvg < 15 || count < 4 || maxAlt < 20)
            lowVisibility = true;
        return lowVisibility;
    }

    public boolean isVisibilityLow() {
        return lowVisibility;
    }

    @Override
    public Date getPeakDate() {
        return peakDate.getTime();
    }

    @Override
    public String getLongName() { return showerLongName; }

    public int getZhr() { return zhr; }

    private double calculateAlt(double latitude, double longitude, Calendar date) {
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        return astroUnitConverter.getAzAlt(this.rightAsc, this.decl, latitude, longitude, date).second;
    }
}
