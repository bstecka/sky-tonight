package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.AudioDataSource;

import java.util.Calendar;

public class NoteRepository implements NoteDataSource {

    private static NoteRepository INSTANCE = null;
    private NoteDataSource mNoteDataSource;

    private NoteRepository(NoteDataSource noteDataSource) {
        mNoteDataSource = noteDataSource;
    }

    public static NoteRepository getInstance(NoteDataSource noteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NoteRepository(noteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void saveFile(Calendar date, String content) {
        mNoteDataSource.saveFile(date, content);
    }

    @Override
    public void readNotesForDay(Calendar selectedDate, GetNoteFilesCallback callback) {
        mNoteDataSource.readNotesForDay(selectedDate, callback);
    }

    @Override
    public void readNotesForWeek(Calendar selectedDate, GetNoteFilesCallback callback) {
        mNoteDataSource.readNotesForWeek(selectedDate, callback);
    }

    @Override
    public void readNotesForMonth(int month, int year, GetNoteFilesCallback callback) {
        mNoteDataSource.readNotesForMonth(month, year, callback);
    }
}
