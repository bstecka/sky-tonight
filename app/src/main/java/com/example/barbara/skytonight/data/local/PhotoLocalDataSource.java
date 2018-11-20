package com.example.barbara.skytonight.data.local;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;

import com.example.barbara.skytonight.data.PhotoDataSource;
import com.example.barbara.skytonight.entity.ImageFile;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PhotoLocalDataSource implements PhotoDataSource {

    private static PhotoLocalDataSource INSTANCE;
    private File storageDir;

    private PhotoLocalDataSource() {}

    private PhotoLocalDataSource(File storageDir) {
        this.storageDir = storageDir;
    }

    public static PhotoLocalDataSource getInstance(File storageDir) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoLocalDataSource(storageDir);
        }
        return INSTANCE;
    }

    public File createImageFile(Calendar selectedDate) {
        String timeStamp;
        if (selectedDate != null)
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(selectedDate.getTime());
        else
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File file;
        try {
            file = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            file = null;
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void readPhotosForDay(Calendar selectedDate, GetImageFileCallback callback) {
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            new GetImageFilesTask(callback).execute(filteredFiles);
        }
    }

    @Override
    public void readPhotosForWeek(final Calendar selectedDate, GetImageFileCallback callback) {
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
                    return selectedDate.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) && selectedDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                }
            });
            new GetImageFilesTask(callback).execute(filteredFiles);
        }
    }

    @Override
    public void readPhotosForMonth(int month, int year, GetImageFileCallback callback) {
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
            new GetImageFilesTask(callback).execute(filteredFiles);
        }
    }

    private static class GetImageFilesTask extends AsyncTask<File[], ImageFile, ArrayList<ImageFile>> {
        GetImageFileCallback callback;

        GetImageFilesTask(GetImageFileCallback callback){
            this.callback = callback;
        }

        private ArrayList<ImageFile> readFiles(File[] files){
            ArrayList<ImageFile> list = new ArrayList<>();
            for (File file : files) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    Matrix rotationMatrix = getRotationMatrix(file.getAbsolutePath());
                    Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMatrix, false);
                    Bitmap scaled = Bitmap.createScaledBitmap(rotated, rotated.getWidth() / 4, rotated.getHeight() / 4, false);
                    ImageFile imageFile = new ImageFile(scaled, file);
                    list.add(imageFile);
                    publishProgress(imageFile);
                }
            }
            return list;
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
        protected void onPostExecute(ArrayList<ImageFile> files) {
        }

        @Override
        protected ArrayList<ImageFile> doInBackground(File[]... files) {
            return readFiles(files[0]);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(ImageFile... values) {
            callback.onDataLoaded(values[0]);
        }
    }
}
