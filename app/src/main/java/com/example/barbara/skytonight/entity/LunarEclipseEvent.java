package com.example.barbara.skytonight.entity;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_PARTIAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_PENUMBRAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_TOTAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_ANNULAR;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_HYBRID;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_PARTIAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_TOTAL;

public class LunarEclipseEvent extends AstroEvent {

    private int eclipseType;
    private Calendar partialBegins;
    private Calendar partialEnds;
    private Calendar penunmbralBegins;
    private Calendar penunmbralEnds;
    private Calendar totalBegins;
    private Calendar totalEnds;

    public LunarEclipseEvent() {
        super();
    }

    public LunarEclipseEvent(int id, Calendar startDate, int eclipseType, double latitude, double longitude) {
        super(id, "ecl_lun_" + eclipseType, startDate, startDate, latitude, longitude);
        this.eclipseType = eclipseType;
    }

    public LunarEclipseEvent(int id, Calendar startDate, int eclipseType, double latitude, double longitude, Calendar partialBegins, Calendar partialEnds, Calendar penunmbralBegins, Calendar penunmbralEnds, Calendar totalBegins, Calendar totalEnds) {
        super(id, "ecl_lun_" + eclipseType, startDate, startDate, latitude, longitude);
        this.eclipseType = eclipseType;
        this.partialBegins = partialBegins;
        this.partialEnds = partialEnds;
        this.totalBegins = totalBegins;
        this.totalEnds = totalEnds;
        this.penunmbralBegins = penunmbralBegins;
        this.penunmbralEnds = penunmbralEnds;
    }

    @Override
    public String getLongName() {
        String typeStr = "";
        switch (eclipseType) {
            case LUNAR_PENUMBRAL:
                typeStr = "Penumbral";
                break;
            case LUNAR_PARTIAL:
                typeStr = "Partial";
                break;
            case LUNAR_TOTAL:
                typeStr = "Total";
                break;
        }
        return typeStr + " Lunar Eclipse";
    }

    public int getEclipseType(){
        return eclipseType;
    }

    public Calendar getPartialBegins() {
        return partialBegins;
    }

    public Calendar getPartialEnds() {
        return partialEnds;
    }

    public Calendar getPenunmbralBegins() {
        return penunmbralBegins;
    }

    public Calendar getPenunmbralEnds() {
        return penunmbralEnds;
    }

    public Calendar getTotalBegins() {
        return totalBegins;
    }

    public Calendar getTotalEnds() {
        return totalEnds;
    }

    @Override
    public String toString() {
        return getStringForCalendar(super.getPeak()) + " " + getStringForCalendar(partialBegins) +
                " " + getStringForCalendar(partialEnds) +
                " " + getStringForCalendar(penunmbralBegins) +
                " " + getStringForCalendar(penunmbralEnds) +
                " " + getStringForCalendar(totalBegins)
                + " " + getStringForCalendar(totalEnds);
    }

    private String getStringForCalendar(Calendar calendar) {
        if (calendar == null)
            return "[]";
        else
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
    }
}
