package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.location.Location;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.data.AstroEvent;

import java.util.ArrayList;

public interface EventsContract {

    interface GetUserLocationCallback {
        void onDataLoaded(Location location);
        void onDataNotAvailable();
    }

    interface View extends BaseView<Presenter> {
        void updateList(ArrayList<AstroEvent> list);
        void clearList();
        Activity getCurrentActivity();
    }

    interface Presenter extends BasePresenter {
        void getUserLocation(GetUserLocationCallback callback);
        void showEventsForMonth(int month, int year);
    }
}
