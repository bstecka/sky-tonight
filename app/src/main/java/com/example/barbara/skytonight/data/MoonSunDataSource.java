package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.entity.MoonSunData;

import java.util.Calendar;

public interface MoonSunDataSource {

    interface GetMoonSunDataCallback {
        void onDataLoaded(MoonSunData moonSunData);
        void onDataNotAvailable();
    }

    void getMoonSunData(Calendar time, final double latitude, final double longitude, final GetMoonSunDataCallback callback);
}
