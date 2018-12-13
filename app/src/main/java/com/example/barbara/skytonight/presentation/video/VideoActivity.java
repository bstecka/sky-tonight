package com.example.barbara.skytonight.presentation.video;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.AppConstants;
import com.example.barbara.skytonight.presentation.core.CalendarContract;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

import java.util.Calendar;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean weekMode = false;
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
            if (bundle.getInt("type") == CalendarContract.TAB_TYPE_WEEK)
                weekMode = true;
        }
        setContentView(R.layout.activity_video);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.videoFragment);
        VideoFragment videoFragment = (VideoFragment) currentFragment;
        if (videoFragment != null) {
            videoFragment.setPresenter(new VideoPresenter(videoFragment));
            videoFragment.setSelectedDate(selectedDate);
            if (selectedMonth != null)
                videoFragment.setSelectedMonthYear(selectedMonth, selectedYear);
            if (weekMode)
                videoFragment.setWeekMode(true);
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
