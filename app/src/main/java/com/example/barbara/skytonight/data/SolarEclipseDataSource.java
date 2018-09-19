package com.example.barbara.skytonight.data;

import java.util.List;

public interface SolarEclipseDataSource {

    interface GetSolarEclipsesCallback {

        void onDataLoaded(List<SolarEclipseEvent> events);
        void onDataNotAvailable();
    }

    void getSolarEclipses(final double latitude, final double longitude, GetSolarEclipsesCallback callback);
}
