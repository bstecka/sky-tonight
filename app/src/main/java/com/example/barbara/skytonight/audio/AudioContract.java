package com.example.barbara.skytonight.audio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.photos.ImageFile;

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
    }

    interface Presenter extends BasePresenter {
        void startRecording();
        void stopRecording();
    }
}
