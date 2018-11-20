package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.entity.NoteFile;

import java.util.Calendar;

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
