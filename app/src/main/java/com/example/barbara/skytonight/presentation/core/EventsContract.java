package com.example.barbara.skytonight.presentation.core;

import android.app.Activity;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;
import com.example.barbara.skytonight.entity.AstroEvent;

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
