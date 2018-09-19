package com.example.barbara.skytonight.data;

import java.util.Calendar;

public class SolarEclipseEvent extends AstroEvent {

    private int eclipseType;

    public SolarEclipseEvent() {
        super();
    }

    public SolarEclipseEvent(int id, String name, Calendar startDate, int eclipseType) {
        super(id, "ecl_sol_" + eclipseType, startDate, startDate);
        this.eclipseType = eclipseType;
    }

    public int getEclipseType(){
        return eclipseType;
    }
}
