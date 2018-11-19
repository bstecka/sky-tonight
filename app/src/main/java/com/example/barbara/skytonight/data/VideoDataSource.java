package com.example.barbara.skytonight.data;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public interface VideoDataSource {
    interface GetVideoFilesCallback {
        void onDataLoaded(List<File> files);
        void onDataNotAvailable();
    }

    File createFile(Calendar date);
    void readFilesForDay(Calendar date, VideoDataSource.GetVideoFilesCallback callback);
    void readFilesForWeek(final Calendar date, VideoDataSource.GetVideoFilesCallback callback);
    void readFilesForMonth(int month, int year, VideoDataSource.GetVideoFilesCallback callback);
}
