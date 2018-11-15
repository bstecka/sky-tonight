package com.example.barbara.skytonight.presentation.core;

import android.util.Log;

import com.example.barbara.skytonight.presentation.core.CoreContract;
import com.example.barbara.skytonight.presentation.core.TodayPresenter;

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
