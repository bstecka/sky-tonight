package com.example.barbara.skytonight.data.local;
import android.content.Context;
import android.os.Environment;

import com.example.barbara.skytonight.data.AudioDataSource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AudioLocalDataSource implements AudioDataSource {

    private static AudioLocalDataSource INSTANCE;
    private File storageDir;

    private AudioLocalDataSource() {}

    private AudioLocalDataSource(File storageDir) {
        this.storageDir = storageDir;
    }

    private AudioLocalDataSource(Context context) {
        this.storageDir = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
    }

    public static AudioLocalDataSource getInstance(File storageDir) {
        if (INSTANCE == null) {
            INSTANCE = new AudioLocalDataSource(storageDir);
        }
        return INSTANCE;
    }

    public static AudioLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AudioLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public File createFile(Calendar date) {
        String timeStamp;
        if (date != null)
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date.getTime());
        else
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String imageFileName = "3GP_" + timeStamp + "_";
        File file = null;
        try {
            file = File.createTempFile(imageFileName, ".3gp", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void readFilesForDay(Calendar date, GetAudioFilesCallback callback) {
        ArrayList<File> list = new ArrayList<>();
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                list.add(file);
            callback.onDataLoaded(list);
        }
    }

    @Override
    public void readFilesForWeek(final Calendar date, GetAudioFilesCallback callback) {
        ArrayList<File> list = new ArrayList<>();
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    try {
                        Date date = sdf.parse(name.substring(4, 13));
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return date.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) && date.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                }
            });
            for (File file : filteredFiles)
                list.add(file);
            callback.onDataLoaded(list);
        }
    }

    @Override
    public void readFilesForMonth(int month, int year, GetAudioFilesCallback callback) {
        ArrayList<File> list = new ArrayList<>();
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
            callback.onDataLoaded(list);
        }
    }
}
