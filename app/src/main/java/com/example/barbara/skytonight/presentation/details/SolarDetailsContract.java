package com.example.barbara.skytonight.presentation.details;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

public interface SolarDetailsContract {

    interface View extends BaseView<Presenter> {
        Context getContext();
        Activity getViewActivity();
        void setImage(String imageUrl);
        void setTitle(String name, String date);
        void setTimeLine(String greatestEclipse);
    }

    interface Presenter extends BasePresenter {
    }
}
