package com.example.barbara.skytonight.entity;

import android.support.v4.util.Pair;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static com.example.barbara.skytonight.entity.AstroConstants.GMST_CONST;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_D0;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_H;

public class AstroUnitConverter {

    public Pair<Double, Double> getAzAlt(double rightAsc, double decl, double latitude, double longitude, Calendar date) {
        if (date == null)
            throw new IllegalArgumentException();
        double LST = getLocalSiderealTime(date, longitude);
        double HA = LST - rightAsc + 360;
        double x, y, z, xhor, yhor, zhor;
        x = Math.cos(Math.toRadians(HA)) * Math.cos(Math.toRadians(decl));
        y = Math.sin(Math.toRadians(HA)) * Math.cos(Math.toRadians(decl));
        z = Math.sin(Math.toRadians(decl));
        xhor = x * Math.sin(Math.toRadians(latitude)) - z * Math.cos(Math.toRadians(latitude));
        yhor = y;
        zhor = x * Math.cos(Math.toRadians(latitude)) + z * Math.sin(Math.toRadians(latitude));
        double azimuth = Math.toDegrees(Math.atan2(yhor, xhor)) + 180;
        double altitude = Math.toDegrees(Math.asin(zhor));
        return new Pair<>(azimuth, altitude);
    }

    public double getJulianDate(Calendar date) {
        if (date == null)
            throw new IllegalArgumentException();
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

    public double getLocalSiderealTime(Calendar date, double longitudeDeg)
    {
        if (date == null)
            throw new IllegalArgumentException();
        return (getGMST(date) + longitudeDeg) % 360.0;
    }

    public double getGMST(Calendar date){
        if (date == null)
            throw new IllegalArgumentException();
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

}
