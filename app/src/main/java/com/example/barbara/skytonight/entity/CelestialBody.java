package com.example.barbara.skytonight.entity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static com.example.barbara.skytonight.entity.AstroConstants.GMST_CONST;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_D0;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_H;

public class CelestialBody extends AstroObject {
    private double rightAsc;
    private double decl;
    private double illu;
    private boolean waxing;
    private Double azimuth;
    private Double altitude;

    public CelestialBody() {
        super();
        this.rightAsc = -1;
        this.decl = -1;
        this.azimuth = -1.0;
        this.altitude = -1.0;
    }

    public CelestialBody(int id, String name, double rightAscension, double decl, double illu, boolean waxing, Calendar time) {
        super(id, name, time);
        this.rightAsc = rightAscension;
        this.decl = decl;
        this.illu = illu;
        this.waxing = waxing;
    }

    public int getPhaseId() {
        double phase = 14 * illu/100;
        return waxing ? (int) phase : 14 + (int) phase;
    }

    public double getAltitude(double latitude, double longitude) {
        if (altitude == null)
            calculateAzAlt(latitude, longitude, super.getTime());
        return altitude;
    }

    public String getApproximateDirectionString(){
        if (azimuth == null)
            return "";
        if (azimuth >= 337.5 || azimuth < 22.5)
            return "n";
        if (azimuth >= 22.5 && azimuth < 67.5)
            return "e";
        if (azimuth >= 67.5 && azimuth < 112.5)
            return "e";
        if (azimuth >= 112.5 && azimuth < 157.5)
            return "se";
        if (azimuth >= 157.5 && azimuth < 202.5)
            return "s";
        if (azimuth >= 202.5 && azimuth < 247.5)
            return "sw";
        if (azimuth >= 247.5 && azimuth < 292.5)
            return "w";
        return "nw";
    }

    private void calculateAzAlt(double latitude, double longitude, Calendar date) {
        double LST = getLocalSiderealTime(date, longitude);
        double HA = LST - this.rightAsc + 360;
        double x, y, z, xhor, yhor, zhor;
        x = Math.cos(Math.toRadians(HA)) * Math.cos(Math.toRadians(this.decl));
        y = Math.sin(Math.toRadians(HA)) * Math.cos(Math.toRadians(this.decl));
        z = Math.sin(Math.toRadians(this.decl));
        xhor = x * Math.sin(Math.toRadians(latitude)) - z * Math.cos(Math.toRadians(latitude));
        yhor = y;
        zhor = x * Math.cos(Math.toRadians(latitude)) + z * Math.sin(Math.toRadians(latitude));
        this.azimuth = Math.toDegrees(Math.atan2(yhor, xhor)) + 180;
        this.altitude = Math.toDegrees(Math.asin(zhor));
    }

    private double getGMST(Calendar date){
        Calendar time = date;
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
