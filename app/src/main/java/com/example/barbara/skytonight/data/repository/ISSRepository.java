package com.example.barbara.skytonight.data.repository;

import com.example.barbara.skytonight.data.ISSDataSource;

import java.util.Calendar;

public class ISSRepository implements ISSDataSource {

    private static ISSRepository INSTANCE = null;
    private final ISSDataSource mISSDataSource;

    private ISSRepository(ISSDataSource mISSDataSource) {
        this.mISSDataSource = mISSDataSource;
    }

    public static ISSRepository getInstance(ISSDataSource mISSDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ISSRepository(mISSDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
    
    @Override
    public void getISSObject(Calendar time, double latitude, double longitude, GetISSObjectCallback callback) {
        mISSDataSource.getISSObject(time, latitude, longitude, callback);
    }
}
