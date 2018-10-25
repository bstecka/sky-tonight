package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.location.Location;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.WeatherObject;

import java.util.ArrayList;

public class NewsContract {

    interface View extends BaseView<Presenter> {
        void clearList();
        void updateList(ArrayList<NewsHeadline> list);
        Activity getCurrentActivity();
    }

    interface Presenter extends BasePresenter {
    }
}
