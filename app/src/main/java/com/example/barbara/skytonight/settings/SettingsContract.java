package com.example.barbara.skytonight.settings;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

public class SettingsContract {

    interface View extends BaseView<Presenter> {
        Context getContext();
        Activity getViewActivity();
        void setLanguageSelected(int language);
    }

    interface Presenter extends BasePresenter {
    }
}
