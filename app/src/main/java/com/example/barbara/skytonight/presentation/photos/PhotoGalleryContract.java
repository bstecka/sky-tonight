package com.example.barbara.skytonight.presentation.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.util.ArrayList;
import java.util.Calendar;

public class PhotoGalleryContract {

    interface View extends BaseView<Presenter> {
        void refreshListInView();
        void clearListInView();
        void startPhotoActivity(Intent intent);
        ArrayList<ImageFile> getPhotoList();
        Context getContext();
        Activity getViewActivity();
        Calendar getSelectedDate();
        Integer getSelectedMonth();
        Integer getSelectedYear();
        boolean isWeekModeEnabled();
    }

    interface Presenter extends BasePresenter {
        void dispatchTakePhotoIntent();
    }
}
