package com.example.barbara.skytonight.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

import com.example.barbara.skytonight.R;

public class FullPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);
        ImageViewTouch imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(MyPhotoRecyclerViewAdapter.FILE_PATH);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap != null) {
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, false);
            imageView.setImageBitmap(scaled);
            imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        }
    }
}
