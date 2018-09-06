package com.example.barbara.skytonight.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.TodayRepository;
import com.example.barbara.skytonight.data.remote.AstroObjectsRemoteDataSource;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CoreActivity extends AppCompatActivity implements TodayFragment.OnListFragmentInteractionListener, CalendarFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener {

    private BottomNavigationView bottomNavigationView;
    private MyViewPager viewPager;
    private BottomBarAdapter pagerAdapter;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        viewPager = findViewById(R.id.core_pager);
        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        final Context context = this;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Bundle args = new Bundle();
                            args.putDouble("latitude", location.getLatitude());
                            args.putDouble("longitude", location.getLongitude());
                            final TodayFragment todayFragment = new TodayFragment();
                            todayFragment.setArguments(args);
                            final TodayPresenter presenter = new TodayPresenter(new TodayRepository(new AstroObjectsRemoteDataSource(context)), todayFragment);
                            todayFragment.setPresenter(presenter);
                            NewsFragment newsFragment = new NewsFragment();
                            CalendarFragment calendarFragment = new CalendarFragment();
                            pagerAdapter.addFragments(calendarFragment);
                            pagerAdapter.addFragments(todayFragment);
                            pagerAdapter.addFragments(newsFragment);
                            viewPager.setAdapter(pagerAdapter);
                            bottomNavigationView = findViewById(R.id.bottom_nav);
                            bottomNavigationView.setOnNavigationItemSelectedListener(
                                    new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    }
                });
            }

    @Override
    public void onListFragmentInteraction(String item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
