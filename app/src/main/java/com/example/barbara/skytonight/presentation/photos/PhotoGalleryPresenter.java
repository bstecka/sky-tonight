package com.example.barbara.skytonight.presentation.photos;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import com.example.barbara.skytonight.data.PhotoDataSource;
import com.example.barbara.skytonight.data.repository.PhotoRepository;
import com.example.barbara.skytonight.data.local.PhotoLocalDataSource;
import com.example.barbara.skytonight.entity.ImageFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class PhotoGalleryPresenter implements PhotoGalleryContract.Presenter {

    private final PhotoGalleryContract.View mPhotoGalleryView;
    private PhotoRepository photoRepository;

    public PhotoGalleryPresenter(PhotoGalleryContract.View mPhotoGalleryView) {
        this.mPhotoGalleryView = mPhotoGalleryView;
        File storageDir = mPhotoGalleryView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        this.photoRepository = PhotoRepository.getInstance(PhotoLocalDataSource.getInstance(storageDir));
    }

    @Override
    public void start() {
        mPhotoGalleryView.clearListInView();
        readPhotosAsync();
    }

    @Override
    public void dispatchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mPhotoGalleryView.getViewActivity().getPackageManager()) != null) {
            File photoFile = photoRepository.createImageFile(mPhotoGalleryView.getSelectedDate());
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mPhotoGalleryView.getContext(), "com.example.barbara.skytonight.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mPhotoGalleryView.startPhotoActivity(takePictureIntent);
            }
        }
    }

    private void readPhotosForDay(Calendar selectedDate) {
        final ArrayList<ImageFile> list = mPhotoGalleryView.getPhotoList();
        photoRepository.readPhotosForDay(selectedDate, new PhotoDataSource.GetImageFileCallback() {
            @Override
            public void onDataLoaded(ImageFile file) {
                list.add(file);
                mPhotoGalleryView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readPhotosForWeek(final Calendar selectedDate) {
        final ArrayList<ImageFile> list = mPhotoGalleryView.getPhotoList();
        photoRepository.readPhotosForWeek(selectedDate, new PhotoDataSource.GetImageFileCallback() {
            @Override
            public void onDataLoaded(ImageFile file) {
                list.add(file);
                mPhotoGalleryView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readPhotosForMonth(int month, int year) {
        final ArrayList<ImageFile> list = mPhotoGalleryView.getPhotoList();
        photoRepository.readPhotosForMonth(month, year, new PhotoDataSource.GetImageFileCallback() {
            @Override
            public void onDataLoaded(ImageFile file) {
                list.add(file);
                mPhotoGalleryView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readPhotosAsync() {
        Calendar selectedDate = mPhotoGalleryView.getSelectedDate();
        if (mPhotoGalleryView.isWeekModeEnabled()) {
            readPhotosForWeek(selectedDate);
        }
        else if (selectedDate != null) {
            readPhotosForDay(selectedDate);
        } else if (mPhotoGalleryView.getSelectedMonth() != null) {
            readPhotosForMonth(mPhotoGalleryView.getSelectedMonth(), mPhotoGalleryView.getSelectedYear());
        }
    }

}
