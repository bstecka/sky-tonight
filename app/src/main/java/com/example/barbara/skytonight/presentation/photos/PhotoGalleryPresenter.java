package com.example.barbara.skytonight.presentation.photos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.barbara.skytonight.presentation.photos.ImageFile;
import com.example.barbara.skytonight.presentation.photos.PhotoGalleryContract;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PhotoGalleryPresenter implements PhotoGalleryContract.Presenter {

    private final PhotoGalleryContract.View mPhotoGalleryView;

    public PhotoGalleryPresenter(PhotoGalleryContract.View mPhotoGalleryView) {
        this.mPhotoGalleryView = mPhotoGalleryView;
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
            File photoFile = null;
            try {
                photoFile = createImageFile(mPhotoGalleryView.getViewActivity());
            } catch (IOException ex) {
                Log.e("PhotoGallery", "IOException");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mPhotoGalleryView.getContext(), "com.example.barbara.skytonight.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mPhotoGalleryView.startPhotoActivity(takePictureIntent);
            }
        }
    }

    private void readPhotosAsyncForDay(Calendar selectedDate) {
        File storageDir = mPhotoGalleryView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                new DisplaySingleImageTask(file, mPhotoGalleryView.getPhotoList(), mPhotoGalleryView).execute(file);
        }
    }

    private void readPhotosAsyncForWeek(final Calendar selectedDate) {
        File storageDir = mPhotoGalleryView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    try {
                        Date date = sdf.parse(name.substring(5, 13));
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                    //Log.e("Phtoog", selectedDate.get(Calendar.WEEK_OF_YEAR) + " " + calendar.get(Calendar.WEEK_OF_YEAR) + " " + selectedDate.get(Calendar.YEAR) + " " + calendar.get(Calendar.YEAR));
                    return selectedDate.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) && selectedDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                }
            });
            for (File file : filteredFiles)
                new DisplaySingleImageTask(file, mPhotoGalleryView.getPhotoList(), mPhotoGalleryView).execute(file);
        }
    }

    private void readPhotosAsyncForMonth(int month, int year) {
        File storageDir = mPhotoGalleryView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.MONTH, month);
        selectedDate.set(Calendar.YEAR, year);
        final String timeStamp = new SimpleDateFormat("yyyyMM", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                new DisplaySingleImageTask(file, mPhotoGalleryView.getPhotoList(), mPhotoGalleryView).execute(file);
        }
    }

    private void readPhotosAsync() {
        Calendar selectedDate = mPhotoGalleryView.getSelectedDate();
        if (mPhotoGalleryView.isWeekModeEnabled()) {
            readPhotosAsyncForWeek(selectedDate);
        }
        else if (selectedDate != null) {
            readPhotosAsyncForDay(selectedDate);
        } else if (mPhotoGalleryView.getSelectedMonth() != null) {
            readPhotosAsyncForMonth(mPhotoGalleryView.getSelectedMonth(), mPhotoGalleryView.getSelectedYear());
        }
    }

    private File createImageFile(Activity activity) throws IOException {
        Calendar selectedDate = mPhotoGalleryView.getSelectedDate();
        String timeStamp;
        if (selectedDate != null)
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(selectedDate.getTime());
        else
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private static class DisplaySingleImageTask extends AsyncTask<File, Void, Boolean> {
        PhotoGalleryContract.View view;
        ArrayList<ImageFile> list;
        File file;

        DisplaySingleImageTask(File file, ArrayList<ImageFile> list, PhotoGalleryContract.View view){
            this.file = file;
            this.list = list;
            this.view = view;
        }

        @Override
        protected Boolean doInBackground(File... params) {
            return readFiles(params[0], list);
        }

        private Boolean readFiles(File file, ArrayList<ImageFile> list){
            boolean shouldRefresh = false;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                Matrix rotationMatrix = getRotationMatrix(file.getAbsolutePath());
                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMatrix, false);
                Bitmap scaled = Bitmap.createScaledBitmap(rotated, rotated.getWidth() / 4, rotated.getHeight() / 4, false);
                list.add(new ImageFile(scaled, file));
                shouldRefresh = true;
            }
            return shouldRefresh;
        }

        private Matrix getRotationMatrix(String fileName) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
            }
            return matrix;
        }

        @Override
        protected void onPostExecute(Boolean shouldRefresh) {
            if (shouldRefresh)
                view.refreshListInView();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
