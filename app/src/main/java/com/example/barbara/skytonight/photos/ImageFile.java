package com.example.barbara.skytonight.photos;

import android.graphics.Bitmap;
import android.widget.ImageView;

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
