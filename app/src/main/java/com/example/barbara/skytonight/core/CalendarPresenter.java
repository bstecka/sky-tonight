package com.example.barbara.skytonight.core;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class CalendarPresenter implements CalendarContract.Presenter {

    private final CalendarContract.View mCalendarView;

    public CalendarPresenter(CalendarContract.View mCalendarView) {
        this.mCalendarView = mCalendarView;
    }

    @Override
    public void start() {
        mCalendarView.updateDayInfoText(Calendar.getInstance());
    }

    @Override
    public int getNumberOfPhotos(Calendar date) {
        File storageDir = mCalendarView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date.getTime());
        int count = 0;
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) { return name.contains(timeStamp); }
            });
            for (File file : filteredFiles)
                count++;
        }
        return count;
    }

    @Override
    public int getNumberOfWords(Calendar date) {
        File storageDir = mCalendarView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date.getTime());
        int count = 0;
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) { return name.contains(timeStamp); }
            });
            for (File file : filteredFiles)
                count = readFile(file);
        }
        return count;
    }

    private int readFile(File file){
        Scanner scanner = null;
        int count = 0;
        try {
            scanner = new Scanner(file);
            try {
                while(scanner.hasNext()){
                    scanner.next();
                    count++;
                }
            } finally {
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return count;
    }

}
