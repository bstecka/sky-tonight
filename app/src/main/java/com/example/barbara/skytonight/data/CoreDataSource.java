package com.example.barbara.skytonight.data;
import android.app.Activity;
import android.location.Location;

import java.util.Calendar;

public interface CoreDataSource {

    interface GetUserLocationCallback {
        void onDataLoaded(Location location);
        void onRequestForPermission();
        void onDataNotAvailable();
    }

    void getUserLocation(Activity activity, GetUserLocationCallback callback);
}