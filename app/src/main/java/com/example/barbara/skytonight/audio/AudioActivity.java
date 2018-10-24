package com.example.barbara.skytonight.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.photos.PhotoGalleryFragment;
import com.example.barbara.skytonight.photos.PhotoGalleryPresenter;
import com.example.barbara.skytonight.util.AppConstants;

import java.util.Calendar;

public class AudioActivity extends AppCompatActivity {

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
            if (bundle.getInt("type") == AppConstants.TAB_TYPE_WEEK)
                weekMode = true;
        }
        setContentView(R.layout.activity_audio);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.audioFragment);
        AudioFragment audioFragment = (AudioFragment) currentFragment;
        if (audioFragment == null) {
            audioFragment = new AudioFragment();
            audioFragment.setPresenter(new AudioPresenter(audioFragment));
            audioFragment.setSelectedDate(selectedDate);
            if (selectedMonth != null)
                audioFragment.setSelectedMonthYear(selectedMonth, selectedYear);
            if (weekMode)
                audioFragment.setWeekMode(true);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.audioFragment, audioFragment);
            transaction.commit();
        }
        else {
            audioFragment.setPresenter(new AudioPresenter(audioFragment));
            audioFragment.setSelectedDate(selectedDate);
            if (selectedMonth != null)
                audioFragment.setSelectedMonthYear(selectedMonth, selectedYear);
            if (weekMode)
                audioFragment.setWeekMode(true);
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        boolean noFineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
        if (noFineLocationPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AppConstants.MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("onRequestPermission", "Record audio");
                        AudioFragment audioView = (AudioFragment) getSupportFragmentManager().findFragmentById(R.id.audioFragment);
                        if (audioView != null)
                            audioView.setRecordingPermitted(true);
                        Toast.makeText(getApplicationContext(), R.string.voice_permission_yes, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("onRequestPermission", "Permission denied");
                    AudioFragment audioView = (AudioFragment) getSupportFragmentManager().findFragmentById(R.id.audioFragment);
                    if (audioView != null)
                        audioView.setRecordingPermitted(false);
                    Toast.makeText(getApplicationContext(), R.string.voice_permission_no, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        AudioFragment audioView = (AudioFragment) getSupportFragmentManager().findFragmentById(R.id.audioFragment);
        if (audioView != null)
            audioView.releaseMediaPlayer();
        super.onBackPressed();
    }

}
