package com.example.barbara.skytonight.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.util.AppConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

public class TodayRepository implements TodaysDataSource {

    private static TodayRepository INSTANCE = null;
    private final AstroObjectsDataSource mAstroObjectsRemoteDataSource;

    public TodayRepository(AstroObjectsDataSource astroObjectsRemoteDataSource) {
        mAstroObjectsRemoteDataSource = astroObjectsRemoteDataSource;
    }

    public static TodayRepository getInstance(AstroObjectsDataSource astroObjectsRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TodayRepository(astroObjectsRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private void getAstroObjectFromRemoteRepository(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        mAstroObjectsRemoteDataSource.getAstroObject(time, objectId, callback);
    }

    @Override
    public void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        getAstroObjectFromRemoteRepository(time, objectId, callback);
    }

    @Override
    public void getUserLocation(final Activity activity, final GetUserLocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            callback.onDataNotAvailable();
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity.getApplicationContext());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    callback.onDataLoaded(location);
                else {
                    callback.onDataNotAvailable();
                    Toast.makeText(activity.getApplicationContext(), R.string.core_no_location_toast, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
