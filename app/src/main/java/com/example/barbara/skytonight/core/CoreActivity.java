package com.example.barbara.skytonight.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.TodayRepository;
import com.example.barbara.skytonight.data.remote.AstroObjectsRemoteDataSource;
import com.example.barbara.skytonight.util.AppConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class CoreActivity extends AppCompatActivity implements CoreContract.View, TodayFragment.OnListFragmentInteractionListener, CalendarFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener {

    private CoreContract.Presenter mPresenter;
    private BottomNavigationView bottomNavigationView;
    private MyViewPager viewPager;
    private BottomBarAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        viewPager = findViewById(R.id.core_pager);
        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        final TodayFragment todayFragment = new TodayFragment();
        final TodayPresenter presenter = new TodayPresenter(new TodayRepository(new AstroObjectsRemoteDataSource(this)), todayFragment);
        todayFragment.setPresenter(presenter);
        mPresenter = new CorePresenter(presenter);
        NewsFragment newsFragment = new NewsFragment();
        CalendarFragment calendarFragment = new CalendarFragment();
        pagerAdapter.addFragments(calendarFragment);
        pagerAdapter.addFragments(todayFragment);
        pagerAdapter.addFragments(newsFragment);
        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottombaritem_calendar:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.bottombaritem_today:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.bottombaritem_news:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void setPresenter(CoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mPresenter.refreshLocation();
                        Toast.makeText(getApplicationContext(), R.string.core_yes_location_toast, Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(getApplicationContext(), R.string.core_no_location_toast, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(String item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
