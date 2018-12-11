package com.example.barbara.skytonight.data;
import com.example.barbara.skytonight.entity.ImageFile;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public interface PhotoDataSource {

    interface GetImageFileCallback {
        void onDataLoaded(ImageFile file);
        void onDataNotAvailable();
    }

    void readPhotosForDay(Calendar selectedDate, GetImageFileCallback callback);
    void readPhotosForWeek(final Calendar selectedDate, GetImageFileCallback callback);
    void readPhotosForMonth(int month, int year, GetImageFileCallback callback);
    void deleteFiles(List<File> fileList);
    File createImageFile(Calendar selectedDate);
}
