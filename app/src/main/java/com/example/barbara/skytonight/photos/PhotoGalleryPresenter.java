package com.example.barbara.skytonight.photos;

public class PhotoGalleryPresenter implements PhotoGalleryContract.Presenter {

    private final PhotoGalleryContract.View mPhotoGalleryView;

    public PhotoGalleryPresenter(PhotoGalleryContract.View mPhotoGalleryView) {
        this.mPhotoGalleryView = mPhotoGalleryView;
    }

    @Override
    public void start() {

    }
}
