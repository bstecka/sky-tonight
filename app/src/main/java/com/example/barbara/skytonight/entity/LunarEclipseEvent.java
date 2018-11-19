package com.example.barbara.skytonight.entity;

import java.util.Calendar;

import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_PARTIAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_PENUMBRAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.LUNAR_TOTAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_ANNULAR;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_HYBRID;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_PARTIAL;
import static com.example.barbara.skytonight.entity.EclipseTypes.SOLAR_TOTAL;

public class LunarEclipseEvent extends AstroEvent {

    private int eclipseType;

    public LunarEclipseEvent() {
        super();
    }

    public LunarEclipseEvent(int id, Calendar startDate, int eclipseType) {
        super(id, "ecl_lun_" + eclipseType, startDate, startDate);
        this.eclipseType = eclipseType;
    }

    public int getEclipseType(){
        return eclipseType;
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
}
