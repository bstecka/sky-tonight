package com.example.barbara.skytonight.presentation.details;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.util.Calendar;

public interface LunarDetailsContract {

    interface View extends BaseView<Presenter> {
        Context getContext();
        Activity getViewActivity();
    }

    interface Presenter extends BasePresenter {
    }
}
