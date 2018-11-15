package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.entity.WeatherObject;

import java.util.List;

public interface WeatherDataSource {

    interface GetWeatherObjectsCallback {
        void onDataLoaded(List<WeatherObject> weatherObjects);
        void onDataNotAvailable();
    }

    void getWeatherObjects(double latitude, double longitude, GetWeatherObjectsCallback callback);

}
