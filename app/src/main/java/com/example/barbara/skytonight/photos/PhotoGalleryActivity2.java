package com.example.barbara.skytonight.photos;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.barbara.skytonight.R;

public class PhotoGalleryActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.photoGalleryFragment);
        PhotoGalleryFragment photoGalleryFragment = (PhotoGalleryFragment) currentFragment;
        if (photoGalleryFragment == null) {
            photoGalleryFragment = new PhotoGalleryFragment();
            photoGalleryFragment.setPresenter(new PhotoGalleryPresenter(photoGalleryFragment));
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.photoGalleryFragment, photoGalleryFragment);
            transaction.commit();
        }
        else {
            photoGalleryFragment.setPresenter(new PhotoGalleryPresenter(photoGalleryFragment));
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
