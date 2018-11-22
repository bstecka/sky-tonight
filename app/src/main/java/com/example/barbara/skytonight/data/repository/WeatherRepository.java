package com.example.barbara.skytonight.data.repository;

import com.example.barbara.skytonight.data.WeatherDataSource;

public class WeatherRepository implements WeatherDataSource {

    private static WeatherRepository INSTANCE = null;
    private final WeatherDataSource mWeatherDataSource;

    private WeatherRepository(WeatherDataSource mWeatherDataSource) {
        this.mWeatherDataSource = mWeatherDataSource;
    }

    public static WeatherRepository getInstance(WeatherDataSource mWeatherDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherRepository(mWeatherDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getWeatherObjects(double latitude, double longitude, GetWeatherObjectsCallback callback) {
        mWeatherDataSource.getWeatherObjects(latitude, longitude, callback);
    }
}
