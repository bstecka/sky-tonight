package com.example.barbara.skytonight.data;

import java.util.Calendar;

public interface ISSDataSource {

    interface GetISSObject {
        void onDataLoaded(ISSObject issObject);
        void onDataNotAvailable();
    }

    void getISSObject(Calendar time, double latitude, double longitude, GetISSObject callback);

}
