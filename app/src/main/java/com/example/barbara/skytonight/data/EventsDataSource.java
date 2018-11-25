package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.entity.AstroEvent;

import java.util.List;

public interface EventsDataSource {

    interface GetEventsCallback {
        void onDataLoaded(List<AstroEvent> events);
        void onDataNotAvailable();
    }

    void getEvents(double latitude, double longitude, GetEventsCallback callback);
    void getEvents(double latitude, double longitude, int month, int year, GetEventsCallback callback);
}
