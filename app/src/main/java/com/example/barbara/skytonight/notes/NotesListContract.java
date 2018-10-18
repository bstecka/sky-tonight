package com.example.barbara.skytonight.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.photos.ImageFile;

import java.util.ArrayList;
import java.util.Calendar;

public class NotesListContract {

    interface View extends BaseView<Presenter> {
        void refreshListInView();
        void clearListInView();
        ArrayList<NoteFile> getNotesList();
        Activity getViewActivity();
        Calendar getSelectedDate();
        Integer getSelectedMonth();
        Integer getSelectedYear();
    }

    interface Presenter extends BasePresenter {

    }
}
