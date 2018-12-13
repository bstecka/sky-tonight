package com.example.barbara.skytonight.presentation.core;

public class CorePresenter implements CoreContract.Presenter {

    private TodayPresenter mTodayPresenter;

    public CorePresenter(TodayPresenter todayPresenter) {
        mTodayPresenter = todayPresenter;
    }

    @Override
    public void start() {

    }

    public void refreshLocation() {
        mTodayPresenter.start();
    }
}
