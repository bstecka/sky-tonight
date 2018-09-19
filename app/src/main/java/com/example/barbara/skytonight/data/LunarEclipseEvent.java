package com.example.barbara.skytonight.data;

import java.util.Calendar;

public class LunarEclipseEvent extends AstroEvent {

    private int eclipseType;

    public LunarEclipseEvent() {
        super();
    }

    public LunarEclipseEvent(int id, String name, Calendar startDate, int eclipseType) {
        super(id, "ecl_lun_" + eclipseType, startDate, startDate);
        this.eclipseType = eclipseType;
    }

    public int getEclipseType(){
        return eclipseType;
    }
}
