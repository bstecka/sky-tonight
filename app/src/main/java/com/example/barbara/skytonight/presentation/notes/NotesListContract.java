package com.example.barbara.skytonight.presentation.notes;

import android.app.Activity;

import com.example.barbara.skytonight.entity.NoteFile;
import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotesListContract {

    interface View extends BaseView<Presenter> {
        void refreshListInView();
        void clearListInView();
        ArrayList<NoteFile> getNotesList();
        Activity getViewActivity();
        Calendar getSelectedDate();
        Integer getSelectedMonth();
        Integer getSelectedYear();
        boolean isWeekModeEnabled();
    }

    interface Presenter extends BasePresenter {
        void deleteFiles(List<File> fileList);
    }
}
