package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.location.Location;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.data.AstroObject;

import java.util.ArrayList;

public class TodayContract {

    interface GetUserLocationCallback {
        void onDataLoaded(Location location);
        void onDataNotAvailable();
    }

    interface GetAstroObjectCallback {
        void onDataLoaded(AstroObject object);
        void onDataNotAvailable();
    }

    interface View extends BaseView<Presenter> {
        void updateList(ArrayList<AstroObject> list);
        void clearList();
        void updateList(AstroObject object);
        void refreshLocation(Location location);
        ArrayList<AstroObject> getList();
        Activity getCurrentActivity();
    }

    interface Presenter extends BasePresenter {
        void getUserLocation(GetUserLocationCallback callback);
        void refreshLocationInView(Location location);
    }
}
