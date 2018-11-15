package com.example.barbara.skytonight.entity;

import java.util.Calendar;

import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_PARTIAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_PENUMBRAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_TOTAL;

public class SolarEclipseEvent extends AstroEvent {

    private int eclipseType;

    public SolarEclipseEvent() {
        super();
    }

    public SolarEclipseEvent(int id, Calendar startDate, int eclipseType) {
        super(id, "ecl_sol_" + eclipseType, startDate, startDate);
        this.eclipseType = eclipseType;
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
}
