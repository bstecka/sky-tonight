package com.example.barbara.skytonight.data;

public class WeatherRepository implements WeatherDataSource {

    private static WeatherRepository INSTANCE = null;
    private final WeatherDataSource mWeatherRemoteDataSource;

    private WeatherRepository(WeatherDataSource mWeatherRemoteDataSource) {
        this.mWeatherRemoteDataSource = mWeatherRemoteDataSource;
    }

    public static WeatherRepository getInstance(WeatherDataSource mWeatherRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherRepository(mWeatherRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private void getWeatherObjectsFromRemoteRepository(double latitude, double longitude, GetWeatherObjectsCallback callback) {
        mWeatherRemoteDataSource.getWeatherObjects(1, 2, callback);
    }

    @Override
    public void getWeatherObjects(double latitude, double longitude, GetWeatherObjectsCallback callback) {
        getWeatherObjectsFromRemoteRepository(latitude, longitude, callback);
    }
}
