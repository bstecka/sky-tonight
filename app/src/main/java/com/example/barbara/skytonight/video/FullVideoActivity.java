package com.example.barbara.skytonight.video;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.photos.MyPhotoRecyclerViewAdapter;

import java.io.IOException;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class FullVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);
        VideoView videoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(MyVideoRecyclerViewAdapter.FILE_PATH);
        videoView.setVideoPath(filePath);
        MediaController mediaControls = new MediaController(FullVideoActivity.this);
        videoView.setMediaController(mediaControls);
        videoView.canPause();
        videoView.start();
    }
}
