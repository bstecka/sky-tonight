package com.example.barbara.skytonight.video;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.audio.AudioFragment;
import com.example.barbara.skytonight.audio.AudioPresenter;

import java.util.Calendar;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Calendar selectedDate = Calendar.getInstance();
        Integer selectedMonth = null, selectedYear = null;
        if (bundle != null) {
            if (bundle.getInt("dayOfYear") != 0) {
                selectedDate.set(Calendar.YEAR, bundle.getInt("year"));
                selectedDate.set(Calendar.DAY_OF_YEAR, bundle.getInt("dayOfYear"));
            }
            else if (bundle.getInt("year") != 0) {
                selectedDate = null;
                selectedYear = bundle.getInt("year");
                selectedMonth = bundle.getInt("month");
            }
            else {
                selectedDate = null;
            }
        }
        setContentView(R.layout.activity_video);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.videoFragment);
        VideoFragment videoFragment = (VideoFragment) currentFragment;
        if (videoFragment == null) {
            videoFragment = new VideoFragment();
            videoFragment.setPresenter(new VideoPresenter(videoFragment));
            videoFragment.setSelectedDate(selectedDate);
            if (selectedMonth != null)
                videoFragment.setSelectedMonthYear(selectedMonth, selectedYear);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.videoFragment, videoFragment);
            transaction.commit();
        }
        else {
            videoFragment.setPresenter(new VideoPresenter(videoFragment));
            videoFragment.setSelectedDate(selectedDate);
            if (selectedMonth != null)
                videoFragment.setSelectedMonthYear(selectedMonth, selectedYear);
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
