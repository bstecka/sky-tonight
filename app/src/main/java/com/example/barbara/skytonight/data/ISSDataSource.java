package com.example.barbara.skytonight.data;

import java.util.List;

public interface ISSDataSource {

    interface GetISSObject {
        void onDataLoaded(ISSObject issObject);
        void onDataNotAvailable();
    }

    void getISSObject(double latitude, double longitude, GetISSObject callback);

}
