package com.example.barbara.skytonight.entity;

import android.graphics.Bitmap;

import java.io.File;

public class ImageFile {

    private Bitmap bitmap;
    private File file;

    public ImageFile(Bitmap bitmap, File file) {
        this.bitmap = bitmap;
        this.file = file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public File getFile() {
        return file;
    }

}
