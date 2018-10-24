package com.example.barbara.skytonight.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.photos.ImageFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class NoteContract {

    interface View extends BaseView<Presenter> {
        void setText(String text);
        Context getContext();
        Activity getViewActivity();
        Calendar getSelectedDate();
        boolean isWeekModeEnabled();
    }

    interface Presenter extends BasePresenter {
        void saveFile(String text);
    }
}
