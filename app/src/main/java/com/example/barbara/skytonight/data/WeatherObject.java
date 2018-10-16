package com.example.barbara.skytonight.data;

import java.util.Calendar;

public class WeatherObject {
    private int weatherId;
    private int cloudCoverage;
    private Calendar time;

    public WeatherObject(int weatherId, int cloudCoverage, Calendar time) {
        this.weatherId = weatherId;
        this.cloudCoverage = cloudCoverage;
        this.time = time;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public int getCloudCoverage() {
        return cloudCoverage;
    }

    public String getWeatherIdString() {
        return "weather_condition_" + weatherId;
    }

    public Calendar getTime() {
        return time;
    }

    @Override
    public String toString() {
        return weatherId + " " + cloudCoverage + "% at " + time.getTime().toString();
    }
}
