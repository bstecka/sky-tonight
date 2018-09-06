package com.example.barbara.skytonight.data;
import android.content.Context;

import java.util.Calendar;
import java.util.List;

public interface TodaysDataSource {

    interface GetUserLocationCallback {

        void onDataLoaded(String response, int objectId);

        void onDataNotAvailable();
    }

    void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback);

    void getUserLocation(Context context, GetUserLocationCallback callback);
}