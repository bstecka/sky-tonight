package com.example.barbara.skytonight.data;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public interface AudioDataSource {

    interface GetAudioFilesCallback {
        void onDataLoaded(List<File> files);
        void onDataNotAvailable();
    }

    File createFile(Calendar date);
    void readFilesForDay(Calendar date, GetAudioFilesCallback callback);
    void readFilesForWeek(final Calendar date, GetAudioFilesCallback callback);
    void readFilesForMonth(int month, int year, GetAudioFilesCallback callback);
}
