package com.example.barbara.skytonight.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

import java.util.ArrayList;
import java.util.Calendar;

public class PhotoGalleryContract {

    interface View extends BaseView<Presenter> {
        void refreshListInView();
        void clearListInView();
        void startPhotoActivity(Intent intent);
        void setSelectedDate(Calendar selectedDate);
        ArrayList<ImageFile> getPhotoList();
        Context getContext();
        Activity getViewActivity();
        Calendar getSelectedDate();
    }

    interface Presenter extends BasePresenter {
        void dispatchTakePhotoIntent();
    }
}
