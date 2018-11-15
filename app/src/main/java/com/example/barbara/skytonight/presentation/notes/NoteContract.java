package com.example.barbara.skytonight.presentation.notes;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

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
