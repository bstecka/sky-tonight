package com.example.barbara.skytonight.data;
import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;

public interface TodaysDataSource {

    interface GetUserLocationCallback {

        void onDataLoaded(Location location);
        void onDataNotAvailable();
    }

    void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback);

    void getUserLocation(Activity activity, GetUserLocationCallback callback);
}