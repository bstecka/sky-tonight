package com.example.barbara.skytonight.data.local;
import android.content.Context;
import android.os.Environment;

import com.example.barbara.skytonight.data.VideoDataSource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoLocalDataSource implements VideoDataSource {

    private static VideoLocalDataSource INSTANCE;
    private File storageDir;

    private VideoLocalDataSource() {}

    private VideoLocalDataSource(File storageDir) {
        this.storageDir = storageDir;
    }

    private VideoLocalDataSource(Context context) {
        this.storageDir = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
    }

    public static VideoLocalDataSource getInstance(File storageDir) {
        if (INSTANCE == null) {
            INSTANCE = new VideoLocalDataSource(storageDir);
        }
        return INSTANCE;
    }

    public static VideoLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VideoLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void deleteFiles(final List<File> fileList) {
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    boolean found = false;
                    for (int i = 0; i < fileList.size() && !found; i++){
                        if (fileList.get(i).getName().contains(name))
                            found = true;
                    }
                    return found;
                }
            });
            for (File file : filteredFiles) {
                file.delete();
            }
        }
    }

    @Override
    public File createFile(Calendar date) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String imageFileName = "MP4_" + timeStamp + "_";
        File file = null;
        try {
            file = File.createTempFile(imageFileName, ".mp4", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void readFilesForDay(Calendar date, GetVideoFilesCallback callback) {
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
    public void readFilesForWeek(final Calendar date, GetVideoFilesCallback callback) {
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
    public void readFilesForMonth(int month, int year, GetVideoFilesCallback callback) {
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
