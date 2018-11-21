package com.example.barbara.skytonight.presentation.details;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.util.Calendar;

public interface LunarDetailsContract {

    interface View extends BaseView<Presenter> {
        Context getContext();
        Activity getViewActivity();
        void setDateTextViews(String partB, String partE, String tB, String tE, String gr, String penB, String penE);
        void setMoonTimesTextView(String moonrise, String moonset);
        void setSunTimesTextView(String sunrise, String sunset);
        void setTitle(String name, String date);
    }

    interface Presenter extends BasePresenter {
    }
}
