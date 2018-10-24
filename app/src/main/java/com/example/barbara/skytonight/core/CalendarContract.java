package com.example.barbara.skytonight.core;

import android.app.Activity;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

import java.util.Calendar;

public class CalendarContract {

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
