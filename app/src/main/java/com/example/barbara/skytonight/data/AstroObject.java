package com.example.barbara.skytonight.data;

import android.util.Log;

import com.example.barbara.skytonight.util.AstroConstants;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AstroObject {
    private int id;
    private String name;
    private double rightAsc;
    private double decl;
    private double illu;
    private boolean waxing;
    private Double azimuth;
    private Double altitude;
    private Calendar time;

    public AstroObject() {
        this.id = -1;
        this.name = "";
        this.rightAsc = -1;
        this.decl = -1;
        this.azimuth = -1.0;
        this.altitude = -1.0;
    }

    public AstroObject(int id, String name) {
        this.id = id;
        this.name = name;
        this.rightAsc = -1;
        this.decl = -1;
        this.azimuth = -1.0;
        this.altitude = -1.0;
    }

    public AstroObject(int id, String name, Calendar time) {
        this.id = id;
        this.name = name;
        this.rightAsc = -1;
        this.decl = -1;
        this.azimuth = -1.0;
        this.altitude = -1.0;
        this.time = time;
    }

    public AstroObject(int id, String name, double rightAscension, double decl, double illu, boolean waxing, Calendar time) {
        this.id = id;
        this.name = name;
        this.rightAsc = rightAscension;
        this.decl = decl;
        this.illu = illu;
        this.waxing = waxing;
        this.time = time;
    }

    public AstroObject(int id, String name, double rightAscension, double decl, Calendar time) {
        this.id = id;
        this.name = name;
        this.rightAsc = rightAscension;
        this.decl = decl;
        this.time = time;
    }

    public double getIlluPercentage() { return illu; }

    public String getShortName() { return "astr_obj_" + id; }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public int getPhaseId() {
        double phase = 14 * illu/100;
        return waxing ? (int) phase : 14 + (int) phase;
    }

    public double getAltitude(double latitude, double longitude) {
        if (altitude == null)
            calculateAzAlt(latitude, longitude, time);
        return altitude;
    }

    public double getAzimuth(double latitude, double longitude) {
        if (azimuth == null)
            calculateAzAlt(latitude, longitude, time);
        return azimuth;
    }

    public double getAltitudeForComparator() {
        if (altitude == null)
            return 0.0;
        return altitude;
    }

    @Override
    public String toString() {
        String str = name + " (" + id + "), RA: " + rightAsc + ", Decl: " + decl;
        if (azimuth != null && altitude != null)
            str += ", Azimuth: " + azimuth + ", Alt: " + altitude;
        return str;
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
        double julianDateAdj = getJulianDateAdj(date);
        double LST = getLocalSiderealTime(julianDateAdj, longitude);
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

    private double getGMST(double julianDateAdj)
    {
        double T = (julianDateAdj - 51544.5) / 36525.0;
        double gmst = ((280.46061837 + 360.98564736629 * (julianDateAdj - 51544.5)) + 0.000387933 * T*T - T*T*T/38710000.0) % 360.0;
        return gmst >= 0 ? gmst : gmst + 360.0;
    }

    private double getLocalSiderealTime(double julianDateAdj, double longitudeDeg)
    {
        return (getGMST(julianDateAdj) + longitudeDeg) % 360.0;
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

    private double getJulianDateAdj(Calendar date){
        return getJulianDate(date) - AstroConstants.JDminusMJD;
    }
}
