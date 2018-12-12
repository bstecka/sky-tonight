package com.example.barbara.skytonight.presentation.core;

import android.app.Activity;
import android.location.Location;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;
import com.example.barbara.skytonight.entity.AstroObject;
import com.example.barbara.skytonight.entity.WeatherObject;

public class TodayContract {

    public interface View extends BaseView<Presenter> {
        void clearList();
        void updateList(AstroObject object);
        void deleteFromList(int id);
        void refreshLocationInAdapter(Location location);
        void updateWeatherView(WeatherObject currentWeather);
        void showErrorText();
        void hideErrorText();
        Activity getCurrentActivity();
        int getTimeOverhead();
    }

    public interface Presenter extends BasePresenter {
    }
}
