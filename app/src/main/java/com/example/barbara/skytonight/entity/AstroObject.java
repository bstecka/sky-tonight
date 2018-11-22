package com.example.barbara.skytonight.entity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static com.example.barbara.skytonight.entity.AstroConstants.GMST_CONST;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_D0;
import static com.example.barbara.skytonight.entity.AstroConstants.GMST_FACTOR_H;

public abstract class AstroObject {
    private int id;
    private String name;
    private Calendar time;

    public AstroObject(int id, String name, Calendar time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public String getShortName() { return "astr_obj_" + id; }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public Calendar getTime() { return time; }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
