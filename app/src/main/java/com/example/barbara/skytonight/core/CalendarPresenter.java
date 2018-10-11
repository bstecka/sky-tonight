package com.example.barbara.skytonight.core;

import android.location.Location;
import android.os.Environment;
import android.util.Log;

import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectRepository;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.CoreDataSource;
import com.example.barbara.skytonight.data.CoreRepository;
import com.example.barbara.skytonight.photos.PhotoGalleryPresenter;
import com.example.barbara.skytonight.util.AstroConstants;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class CalendarPresenter implements CalendarContract.Presenter {

    private final CalendarContract.View mCalendarView;

    public CalendarPresenter(CalendarContract.View mCalendarView) {
        this.mCalendarView = mCalendarView;
    }

    @Override
    public void start() {
        mCalendarView.updateDayInfoText(Calendar.getInstance());
    }

    public int getNumberOfPhotos(Calendar date) {
        File storageDir = mCalendarView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        int count = 0;
        if (storageDir != null) {
            File[] allFilesInDir = storageDir.listFiles();
            for (File file : allFilesInDir) {
                Calendar modificationTime = Calendar.getInstance();
                modificationTime.setTime(new Date(file.lastModified()));
                if (date.get(Calendar.DAY_OF_YEAR) == modificationTime.get(Calendar.DAY_OF_YEAR) && date.get(Calendar.YEAR) == modificationTime.get(Calendar.YEAR))
                    count++;
            }
        }
        return count;
    }

}
