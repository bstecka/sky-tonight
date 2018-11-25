package com.example.barbara.skytonight.data;
import android.app.Activity;
import android.content.Context;
import android.location.Location;

public interface LocationDataSource {

    interface GetUserLocationCallback {
        void onDataLoaded(Location location);
        void onRequestForPermission();
        void onDataNotAvailable();
    }

    void getUserLocation(Activity activity, GetUserLocationCallback callback);
    void getUserLocation(Context context, GetUserLocationCallback callback);
}