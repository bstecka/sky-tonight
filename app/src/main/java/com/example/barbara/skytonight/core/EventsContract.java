package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.location.Location;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

public interface EventsContract {

    interface GetUserLocationCallback {
        void onDataLoaded(Location location);
        void onDataNotAvailable();
    }

    interface View extends BaseView<Presenter> {
        Activity getCurrentActivity();
    }

    interface Presenter extends BasePresenter {
        void getUserLocation(GetUserLocationCallback callback);
    }
}
