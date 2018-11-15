package com.example.barbara.skytonight.presentation.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.barbara.skytonight.R;

public class FullVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);
        VideoView videoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(VideoRecyclerViewAdapter.FILE_PATH_TAG);
        videoView.setVideoPath(filePath);
        MediaController mediaControls = new MediaController(FullVideoActivity.this);
        videoView.setMediaController(mediaControls);
        videoView.canPause();
        videoView.start();
    }
}
