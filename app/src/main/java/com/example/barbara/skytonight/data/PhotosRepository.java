package com.example.barbara.skytonight.data;

import java.util.ArrayList;

public class PhotosRepository implements PhotosDataSource {

    private static PhotosRepository INSTANCE = null;
    private final PhotosDataSource mPhotosDataSource;

    private PhotosRepository(PhotosDataSource mPhotosDataSource) {
        this.mPhotosDataSource = mPhotosDataSource;
    }

    public static PhotosRepository getInstance(PhotosDataSource mPhotosDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PhotosRepository(mPhotosDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
