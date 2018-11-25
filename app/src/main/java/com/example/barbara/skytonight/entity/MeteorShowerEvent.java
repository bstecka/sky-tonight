package com.example.barbara.skytonight.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static com.example.barbara.skytonight.entity.AstroConstants.GMST_CONST;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_D0;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_H;

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

    public void calculateVisibility(double latitude, double longitude) {
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
        double LST = getLocalSiderealTime(date, longitude);
        double HA = LST - this.rightAsc + 360;
        double x, z, zhor;
        x = Math.cos(Math.toRadians(HA)) * Math.cos(Math.toRadians(this.decl));
        z = Math.sin(Math.toRadians(this.decl));
        zhor = x * Math.cos(Math.toRadians(latitude)) + z * Math.sin(Math.toRadians(latitude));
        return Math.toDegrees(Math.asin(zhor));
    }

    private double getGMST(Calendar date){
        Calendar time = Calendar.getInstance();
        time.setTime(date.getTime());
        time.setTimeZone(TimeZone.getTimeZone("UT"));
        double H = time.get(Calendar.HOUR_OF_DAY) + time.get(Calendar.MINUTE)/60.0 + time.get(Calendar.SECOND)/3600.0;
        double JD = getJulianDate(time);
        double JD0;
        if (JD - Math.floor(JD) >= 0.5)
            JD0 = Math.floor(JD) + 0.5;
        else
            JD0 = Math.floor(JD) - 0.5;
        double D0 = JD0 - AstroConstants.JD_2000JAN01;
        double GMST = (GMST_CONST + GMST_FACTOR_D0 * D0 + GMST_FACTOR_H * H) * 15;
        return GMST % 360.0;
    }

    private double getLocalSiderealTime(Calendar date, double longitudeDeg)
    {
        return (getGMST(date) + longitudeDeg) % 360.0;
    }

    private double getJulianDate(Calendar date) {
        long t1 = date.getTime().getTime();
        Calendar date2 = Calendar.getInstance();
        date2.set(Calendar.ERA, GregorianCalendar.BC);
        date2.set(Calendar.YEAR, 4713 );
        date2.set(Calendar.MONTH, Calendar.JANUARY);
        date2.set(Calendar.DAY_OF_MONTH, 1);
        date2.set(Calendar.HOUR_OF_DAY, 12);
        date2.set(Calendar.MINUTE, 0);
        date2.set(Calendar.SECOND, 0);
        long t0 = date2.getTime().getTime() + date2.get(Calendar.ZONE_OFFSET) + date2.get(Calendar.DST_OFFSET);
        return (double) (t1 - t0) / (1000 * 60 * 60 * 24);
    }
}
