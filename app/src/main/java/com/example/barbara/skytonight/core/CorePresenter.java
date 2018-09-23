package com.example.barbara.skytonight.core;

import android.util.Log;

public class CorePresenter implements CoreContract.Presenter {

    private TodayPresenter mTodayPresenter;

    public CorePresenter(TodayPresenter todayPresenter) {
        mTodayPresenter = todayPresenter;
    }

    @Override
    public void start() {

    }

    public void refreshLocation() {
        Log.e("CorePresenter", "refresh Location");
        mTodayPresenter.start();
    }
}
