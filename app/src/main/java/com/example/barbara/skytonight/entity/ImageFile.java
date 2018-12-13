package com.example.barbara.skytonight.entity;

import android.graphics.Bitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ImageFile {

    private Bitmap bitmap;
    private File file;
    private Calendar calendar;

    public ImageFile(Bitmap bitmap, File file) {
        this.bitmap = bitmap;
        this.file = file;
        this.calendar = Calendar.getInstance();
        String filePath = file.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        try {
            Date date = sdf.parse(filePath.substring(5, 20));
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public File getFile() {
        return file;
    }

    public Calendar getDate() {
        return calendar;
    }

}
