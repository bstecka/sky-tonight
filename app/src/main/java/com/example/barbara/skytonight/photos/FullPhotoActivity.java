package com.example.barbara.skytonight.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.barbara.skytonight.R;

public class FullPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);
        ImageView imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(MyPhotoRecyclerViewAdapter.FILE_PATH);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap != null) {
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
            imageView.setImageBitmap(scaled);
        } else {
            imageView.setImageResource(R.drawable.icon_mercury);
        }
    }
}
