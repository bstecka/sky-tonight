package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.entity.ISSObject;

import java.util.Calendar;

public interface ISSDataSource {

    interface GetISSObjectCallback {
        void onDataLoaded(ISSObject issObject);
        void onDataNotAvailable();
    }

    void getISSObject(Calendar time, double latitude, double longitude, GetISSObjectCallback callback);

}
