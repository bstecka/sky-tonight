package com.example.barbara.skytonight.presentation.settings;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

public class SettingsContract {

    interface View extends BaseView<Presenter> {
        Context getContext();
        Activity getViewActivity();
        void setUserSelection(String language);
        void setUserChoiceListeners();
    }

    interface Presenter extends BasePresenter {
    }
}
