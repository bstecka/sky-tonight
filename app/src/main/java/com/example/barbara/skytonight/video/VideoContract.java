package com.example.barbara.skytonight.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class VideoContract {

    interface View extends BaseView<Presenter> {
        void refreshListInView();
        void clearListInView();
        ArrayList<File> getFileList();
        Context getContext();
        Activity getViewActivity();
        Calendar getSelectedDate();
        Integer getSelectedMonth();
        Integer getSelectedYear();
        void startVideoActivity(Intent intent);
        boolean isWeekModeEnabled();
    }

    interface Presenter extends BasePresenter {
        void dispatchTakeVideoIntent();
    }
}
