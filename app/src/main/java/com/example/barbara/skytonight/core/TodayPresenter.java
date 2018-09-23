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

    @Override
    public void start() {
        mCoreRepository.getUserLocation(mTodayView.getCurrentActivity(), new CoreDataSource.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                Log.e("TodayPresenter", "onDataLoaded mFusedLocationClient success " + location.getLatitude() + " " + location.getLongitude());
                mTodayView.refreshLocationInAdapter(location);
                showObjects(); //objects are shown asynchronously
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("TodayPresenter", "onDataNotAvailable mFusedLocationClient failure");
                //showObjects();
            }
        });
    }

    private void showObjects(){
        final Calendar time = Calendar.getInstance();
        Log.e("TodayPresenter", time.getTime().toString() + " - showObjects");
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
                    Log.e("TodayPresenter", "AstroObject not available");
                }
            });
        }
    }
}
