package com.example.barbara.skytonight.core;

import android.location.Location;
import android.util.Log;

import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.CoreDataSource;
import com.example.barbara.skytonight.data.CoreRepository;
import com.example.barbara.skytonight.data.TodayRepository;
import com.example.barbara.skytonight.util.AstroConstants;
import java.util.Calendar;

public class TodayPresenter implements TodayContract.Presenter {

    private final TodayRepository mTodayRepository;
    private final CoreRepository mCoreRepository;
    private final TodayContract.View mTodayView;

    public TodayPresenter(TodayRepository mTodayRepository, CoreRepository mCoreRepository, TodayContract.View mTodayView) {
        this.mTodayRepository = mTodayRepository;
        this.mCoreRepository = mCoreRepository;
        this.mTodayView = mTodayView;
    }

    public void refreshLocationInView(Location location){
        mTodayView.refreshLocation(location);
    }

    public void getUserLocation(final TodayContract.GetUserLocationCallback callback) {
        System.out.println("PRINTLN getUserLocation");
        Log.e("TodayPresenter", "Call mFusedLocationClient");
        mCoreRepository.getUserLocation(mTodayView.getCurrentActivity(), new CoreDataSource.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                Log.e("TodayPresenter", "onDataLoaded mFusedLocationClient success " + location.getLatitude() + " " + location.getLongitude());
                System.out.println("Today Presenter onDataLoaded mFusedLocationClient success " + location.getLongitude() + " " + location.getLongitude());
                callback.onDataLoaded(location);
                Log.e("TodayPresenter", "Callback fired");
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("TodayPresenter", "onDataNotAvailable mFusedLocationClient failure");
                System.out.print("Today Presenter onDataNotAvailable");
                callback.onDataNotAvailable();
                Log.e("TodayPresenter", "Callback fired");
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
        for (int id: objectIds){
            mTodayRepository.getAstroObject(time, id, new AstroObjectsDataSource.GetAstroObjectsCallback() {
                @Override
                public void onDataLoaded(AstroObject object) {
                    mTodayView.updateList(object);
                }
                @Override
                public void onDataNotAvailable() {
                    Log.e("Presenter", "DataNotAvailable");
                }
            });
        }
    }
}
