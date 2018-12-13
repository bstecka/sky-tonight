package com.example.barbara.skytonight.entity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteFile implements Serializable {

    private String content;
    private File file;
    private Calendar calendar;

    public NoteFile(String content, File file) {
        this.content = content;
        this.file = file;
        this.calendar = Calendar.getInstance();
        String filePath = file.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        try {
            Date date = sdf.parse(filePath.substring(4, 19));
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public File getFile() {
        return file;
    }

    public Calendar getDate() {
        return calendar;
    }

}
