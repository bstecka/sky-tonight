package com.example.barbara.skytonight.presentation.core;

import android.app.Activity;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.util.Calendar;

public class CalendarContract {

    public static int TAB_TYPE_DAY = 1;
    public static int TAB_TYPE_WEEK = 2;
    public static int TAB_TYPE_MONTH = 3;

    interface View extends BaseView<Presenter> {
        Activity getViewActivity();
        void updateDayInfoText(Calendar date);
    }

    interface Presenter extends BasePresenter {
        int getNumberOfPhotos(Calendar date);
        int getNumberOfWords(Calendar date);
        int getNumberOfVoiceNotes(Calendar date);
        int getNumberOfVideos(Calendar date);
    }
}
