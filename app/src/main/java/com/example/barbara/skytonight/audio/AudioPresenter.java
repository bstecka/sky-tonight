package com.example.barbara.skytonight.audio;

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

import com.example.barbara.skytonight.photos.ImageFile;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AudioPresenter implements AudioContract.Presenter {

    private final AudioContract.View mAudioView;

    public AudioPresenter(AudioContract.View mAudioView) {
        this.mAudioView = mAudioView;
    }

    @Override
    public void start() {
        Log.e("Presenter", "start");
        mAudioView.clearListInView();
        readFiles();
    }

    private void readFilesForDay(Calendar selectedDate) {
        ArrayList<File> list = mAudioView.getFileList();
        File storageDir = mAudioView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    Log.e("Presenter", name);
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                list.add(file);
            mAudioView.refreshListInView();
        }
    }

    private void readFilesForWeek() {
        Log.e("Presenter", "Read week");
        ArrayList<File> list = mAudioView.getFileList();
        File storageDir = mAudioView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    Calendar now = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    try {
                        Log.e("Presenter", name + " " + name.substring(4, 13));
                        Date date = sdf.parse(name.substring(4, 13));
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e("Presenter", "error");
                        return false;
                    }
                    Log.e("Presenter",now.get(Calendar.WEEK_OF_YEAR) + " " + calendar.get(Calendar.WEEK_OF_YEAR));
                    return now.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) && now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                }
            });
            for (File file : filteredFiles) {
                list.add(file);
                Log.e("Presenter", file.getName() + " " + list.size());
            }
            mAudioView.refreshListInView();
        }
    }

    private void readFilesForMonth(int month, int year) {
        ArrayList<File> list = mAudioView.getFileList();
        File storageDir = mAudioView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
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
                list.add(file);
            mAudioView.refreshListInView();
        }
    }

    private void readFiles() {
        Calendar selectedDate = mAudioView.getSelectedDate();
        if (selectedDate != null) {
            readFilesForDay(selectedDate);
        } else if (mAudioView.getSelectedMonth() != null) {
            readFilesForMonth(mAudioView.getSelectedMonth(), mAudioView.getSelectedYear());
        } else {
            readFilesForWeek();
        }
    }

    private File createFile(Activity activity) throws IOException {
        Calendar selectedDate = mAudioView.getSelectedDate();
        String timeStamp;
        if (selectedDate != null)
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(selectedDate.getTime());
        else
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String imageFileName = "3GP_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        return File.createTempFile(imageFileName, ".3gp", storageDir);
    }

}
