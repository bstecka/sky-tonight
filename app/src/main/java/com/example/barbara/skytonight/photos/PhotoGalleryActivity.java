package com.example.barbara.skytonight.photos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.barbara.skytonight.R;

import java.util.Calendar;

public class PhotoGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Calendar selectedDate = Calendar.getInstance();
        if (bundle != null) {
            selectedDate.set(Calendar.YEAR, bundle.getInt("year"));
            selectedDate.set(Calendar.DAY_OF_YEAR, bundle.getInt("dayOfYear"));
        }
        setContentView(R.layout.activity_photo_gallery);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.photoGalleryFragment);
        PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) currentFragment;
        if (photoGalleryFragment == null) {
            photoGalleryFragment = new PhotoGalleryFragment();
            photoGalleryFragment.setPresenter(new PhotoGalleryPresenter(photoGalleryFragment));
            photoGalleryFragment.setSelectedDate(selectedDate);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.photoGalleryFragment, photoGalleryFragment);
            transaction.commit();
        }
        else {
            photoGalleryFragment.setPresenter(new PhotoGalleryPresenter(photoGalleryFragment));
            photoGalleryFragment.setSelectedDate(selectedDate);
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
