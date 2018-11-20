package com.example.barbara.skytonight.data;

import java.io.File;
import java.util.Calendar;

public class PhotoRepository implements PhotoDataSource {

    private static PhotoRepository INSTANCE = null;
    private PhotoDataSource mPhotoDataSource;

    private PhotoRepository(PhotoDataSource photoDataSource) {
        mPhotoDataSource = photoDataSource;
    }

    public static PhotoRepository getInstance(PhotoDataSource photoDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoRepository(photoDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void readPhotosForDay(Calendar selectedDate, GetImageFileCallback callback) {
        mPhotoDataSource.readPhotosForDay(selectedDate, callback);
    }

    @Override
    public void readPhotosForWeek(Calendar selectedDate, GetImageFileCallback callback) {
        mPhotoDataSource.readPhotosForWeek(selectedDate, callback);
    }

    @Override
    public void readPhotosForMonth(int month, int year, GetImageFileCallback callback) {
        mPhotoDataSource.readPhotosForMonth(month, year, callback);
    }

    @Override
    public File createImageFile(Calendar selectedDate) {
        return mPhotoDataSource.createImageFile(selectedDate);
    }
}
