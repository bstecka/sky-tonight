package com.example.barbara.skytonight.core;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.TodayRepository;
import com.example.barbara.skytonight.data.TodaysDataSource;
import com.example.barbara.skytonight.util.AstroConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodayPresenter implements TodayContract.Presenter{

    private final TodayRepository mTodayRepository;
    private final TodayContract.View mTodayView;

    public TodayPresenter(TodayRepository mTodayRepository, TodayContract.View mTodayView) {
        Log.e("Presenter", "Presenter init");
        this.mTodayRepository = mTodayRepository;
        this.mTodayView = mTodayView;
    }

    public void refreshLocationInView(Location location){
        mTodayView.refreshLocation(location);
    }

    public void getUserLocation(final TodayContract.GetUserLocationCallback callback) {
        mTodayRepository.getUserLocation(mTodayView.getCurrentActivity(), new TodaysDataSource.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                callback.onDataLoaded(location);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void start() {
        showObjects();
    }

    private void showObjects(){
        final Calendar time = Calendar.getInstance();
        Log.e("Presenter", time.getTime().toString() + " - showObjects");
        int [] objectIds = AstroConstants.ASTRO_OBJECT_IDS;
        mTodayView.clearList();
        for (int i = 0; i < objectIds.length; i++) {
            mTodayRepository.getAstroObject(time, objectIds[i], new AstroObjectsDataSource.GetAstroObjectsCallback() {
                @Override
                public void onDataLoaded(AstroObject object) {
                    mTodayView.updateList(object);
                }
                @Override
                public void onDataNotAvailable() {
                    Log.e("Presenter", "onDataNotAvailable");
                }
            });
        }
    }
}
