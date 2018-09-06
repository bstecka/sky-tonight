package com.example.barbara.skytonight.data;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;

public class TodayRepository implements TodaysDataSource {

    private static TodayRepository INSTANCE = null;
    private final AstroObjectsDataSource mAstroObjectsRemoteDataSource;
    private FusedLocationProviderClient mFusedLocationClient;

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

    public void getAstroObjectFromRemoteRepository(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        mAstroObjectsRemoteDataSource.getAstroObject(time, objectId, callback);
    }

    @Override
    public void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        getAstroObjectFromRemoteRepository(time, objectId, callback);
    }

    @Override
    public void getUserLocation(Context context, GetUserLocationCallback callback) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    createFragments(location);
            }
        });
    }


}
