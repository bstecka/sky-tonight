package com.example.barbara.skytonight.presentation.audio;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class AudioContract {

    interface View extends BaseView<Presenter> {
        void refreshListInView();
        void clearListInView();
        ArrayList<File> getFileList();
        Context getContext();
        Activity getViewActivity();
        Calendar getSelectedDate();
        Integer getSelectedMonth();
        Integer getSelectedYear();
        boolean isWeekModeEnabled();
    }

    interface Presenter extends BasePresenter {
        void startRecording();
        void stopRecording();
    }
}
