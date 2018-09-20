package com.example.barbara.skytonight.data;

import java.util.Calendar;
import java.util.List;

public interface LunarEclipseDataSource {

    interface GetLunarEclipsesCallback {

        void onDataLoaded(List<LunarEclipseEvent> events);
        void onDataNotAvailable();
    }

    void getLunarEclipses(double latitude, double longitude, GetLunarEclipsesCallback callback);

    void getLunarEclipses(double latitude, double longitude, int month, int year, GetLunarEclipsesCallback callback);
}
