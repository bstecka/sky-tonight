package com.example.barbara.skytonight.presentation.notes;

import android.os.Environment;

import com.example.barbara.skytonight.data.NoteDataSource;
import com.example.barbara.skytonight.data.repository.NoteRepository;
import com.example.barbara.skytonight.data.local.NoteLocalDataSource;
import com.example.barbara.skytonight.entity.NoteFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotesListPresenter implements NotesListContract.Presenter {

    private final NotesListContract.View mNotesListView;
    private NoteRepository noteRepository;

    public NotesListPresenter(NotesListContract.View mNotesListView) {
        this.mNotesListView = mNotesListView;
        File storageDir = mNotesListView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        this.noteRepository = NoteRepository.getInstance(NoteLocalDataSource.getInstance(storageDir));
    }

    @Override
    public void start() {
        mNotesListView.clearListInView();
        readNotesAsync();
    }

    @Override
    public void deleteFiles(List<File> fileList){
        noteRepository.deleteFiles(fileList);
        mNotesListView.clearListInView();
        readNotesAsync();
    }

    private void readNotesForDay(Calendar selectedDate) {
        mNotesListView.clearListInView();
        final ArrayList<NoteFile> list = mNotesListView.getNotesList();
        noteRepository.readNotesForDay(selectedDate, new NoteDataSource.GetNoteFilesCallback() {
            @Override
            public void onDataLoaded(NoteFile file) {
                list.add(file);
                mNotesListView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readNotesForWeek(final Calendar selectedDate) {
        mNotesListView.clearListInView();
        final ArrayList<NoteFile> list = mNotesListView.getNotesList();
        noteRepository.readNotesForWeek(selectedDate, new NoteDataSource.GetNoteFilesCallback() {
            @Override
            public void onDataLoaded(NoteFile file) {
                list.add(file);
                mNotesListView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readNotesForMonth(int month, int year) {
        mNotesListView.clearListInView();
        final ArrayList<NoteFile> list = mNotesListView.getNotesList();
        noteRepository.readNotesForMonth(month, year, new NoteDataSource.GetNoteFilesCallback() {
            @Override
            public void onDataLoaded(NoteFile file) {
                list.add(file);
                mNotesListView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readNotesAsync() {
        Calendar selectedDate = mNotesListView.getSelectedDate();
        if (mNotesListView.isWeekModeEnabled()) {
            readNotesForWeek(selectedDate);
        } else if (selectedDate != null) {
            readNotesForDay(selectedDate);
        } else if (mNotesListView.getSelectedMonth() != null) {
            readNotesForMonth(mNotesListView.getSelectedMonth(), mNotesListView.getSelectedYear());
        }
    }

}
