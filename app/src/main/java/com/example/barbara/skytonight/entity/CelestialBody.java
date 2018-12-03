package com.example.barbara.skytonight.entity;

import android.support.v4.util.Pair;

import java.util.Calendar;

public class CelestialBody extends AstroObject {
    private double rightAsc;
    private double decl;
    private double illu;
    private boolean waxing;
    private Double azimuth;
    private Double altitude;

    public CelestialBody() {
        super(-1, "", null);
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

    public double getAzimuth(double latitude, double longitude) {
        if (azimuth == null)
            calculateAzAlt(latitude, longitude, super.getTime());
        return azimuth;
    }

    public String getApproximateDirectionString(){
        if (azimuth == null)
            return "";
        if (azimuth > 360.0 || azimuth < 0.0)
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
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Pair<Double, Double> azAlt = astroUnitConverter.getAzAlt(this.rightAsc, this.decl, latitude, longitude, date);
        this.azimuth = azAlt.first;
        this.altitude = azAlt.second;
    }
}
