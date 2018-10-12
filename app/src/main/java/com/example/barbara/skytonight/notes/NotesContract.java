package com.example.barbara.skytonight.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.photos.ImageFile;

import java.util.ArrayList;
import java.util.Calendar;

public class NotesContract {

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
