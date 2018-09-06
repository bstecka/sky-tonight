package com.example.barbara.skytonight.core;

import android.location.Location;

public class CorePresenter implements CoreContract.Presenter {

    TodayPresenter mTodayPresenter;

    public CorePresenter(TodayPresenter todayPresenter) {
        mTodayPresenter = todayPresenter;
    }

    @Override
    public void start() {

    }

    public void refreshLocation() {
        mTodayPresenter.getUserLocation(new TodayContract.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                mTodayPresenter.refreshLocationInView(location);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
