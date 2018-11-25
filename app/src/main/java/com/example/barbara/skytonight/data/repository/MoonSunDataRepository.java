package com.example.barbara.skytonight.data.repository;
import com.example.barbara.skytonight.data.MoonSunDataSource;

import java.util.Calendar;

public class MoonSunDataRepository implements MoonSunDataSource {

    private static MoonSunDataRepository INSTANCE = null;
    private final MoonSunDataSource mMoonDataSource;

    private MoonSunDataRepository(MoonSunDataSource mMoonDataSource) {
        this.mMoonDataSource = mMoonDataSource;
    }

    public static MoonSunDataRepository getInstance(MoonSunDataSource moonSunDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoonSunDataRepository(moonSunDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getMoonSunData(Calendar time, double latitude, double longitude, GetMoonSunDataCallback callback) {
        mMoonDataSource.getMoonSunData(time, latitude, longitude, callback);
    }
}
