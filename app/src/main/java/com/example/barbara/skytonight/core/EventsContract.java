package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.location.Location;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.data.AstroEvent;

import java.util.ArrayList;

public interface EventsContract {

    interface View extends BaseView<Presenter> {
        void updateList(ArrayList<AstroEvent> list);
        void clearList();
        Activity getCurrentActivity();
    }

    interface Presenter extends BasePresenter {
        void showEventsForMonth(int month, int year);
    }
}
