package com.example.barbara.skytonight.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MoonSunData {

    private Calendar moonrise;
    private Calendar moonset;
    private Calendar sunrise;
    private Calendar sunset;

    public MoonSunData(Calendar moonrise, Calendar moonset, Calendar sunrise, Calendar sunset) {
        this.moonrise = moonrise;
        this.moonset = moonset;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public MoonSunData() {}

    public Calendar getMoonrise() {
        return moonrise;
    }

    public Calendar getMoonset() {
        return moonset;
    }

    public Calendar getSunrise() {
        return sunrise;
    }

    public Calendar getSunset() {
        return sunset;
    }

    @Override
    public String toString() {
        DateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(moonrise.getTime()) + " " + sdf.format(moonset.getTime());
    }
}
