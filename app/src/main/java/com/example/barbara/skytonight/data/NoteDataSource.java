package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.presentation.notes.NoteFile;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public interface NoteDataSource {

    interface GetNoteFilesCallback {
        void onDataLoaded(NoteFile file);
        void onDataNotAvailable();
    }

    void readNotesForDay(Calendar selectedDate, GetNoteFilesCallback callback);
    void readNotesForWeek(final Calendar selectedDate, GetNoteFilesCallback callback);
    void readNotesForMonth(int month, int year, GetNoteFilesCallback callback);
    void saveFile(Calendar date, String content);
}
