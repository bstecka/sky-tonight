package com.example.barbara.skytonight.data;

import android.util.Log;

import java.util.Calendar;

public class AstroObjectMoon extends AstroObject {
    private double illu;

    public AstroObjectMoon(int id, String name, double rightAscension, double decl, Calendar time, double illu) {
        super(id, name.substring(0, 4), rightAscension, decl, time);
        this.illu = illu;
    }

    public double getIlluPercentage() {
        return illu;
    }
}
