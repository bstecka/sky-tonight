package com.example.barbara.skytonight.data;

import java.util.List;

public interface MeteorShowerDataSource {

    interface GetMeteorShowersCallback {
        void onDataLoaded(List<MeteorShowerEvent> events);
        void onDataNotAvailable();
    }

    void getMeteorShowers(double latitude, double longitude, GetMeteorShowersCallback callback);

    void getMeteorShowers(double latitude, double longitude, int month, int year, GetMeteorShowersCallback callback);
}
