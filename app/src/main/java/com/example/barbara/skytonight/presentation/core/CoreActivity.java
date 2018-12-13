package com.example.barbara.skytonight.presentation.core;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.AstroEvent;
import com.example.barbara.skytonight.presentation.InfoActivity;
import com.example.barbara.skytonight.presentation.settings.SettingsActivity;
import com.example.barbara.skytonight.AppConstants;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

public class CoreActivity extends AppCompatActivity implements CoreContract.View,
        TodayFragment.OnListFragmentInteractionListener, CalendarFragment.OnFragmentInteractionListener,
        NewsFragment.OnListFragmentInteractionListener, EventsFragment.OnListFragmentInteractionListener {

    private CoreContract.Presenter mCorePresenter;
    private CoreViewPager viewPager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        final Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.core_option_today);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.core_pager);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(createBottomBarAdapter());
        viewPager.setCurrentItem(1);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottombaritem_calendar:
                        viewPager.setCurrentItem(0);
                        toolbar.setTitle(R.string.core_option_calendar);
                        return true;
                    case R.id.bottombaritem_today:
                        viewPager.setCurrentItem(1);
                        toolbar.setTitle(R.string.core_option_today);
                        return true;
                    case R.id.bottombaritem_events:
                        viewPager.setCurrentItem(2);
                        toolbar.setTitle(R.string.core_option_events);
                        return true;
                    case R.id.bottombaritem_news:
                        viewPager.setCurrentItem(3);
                        toolbar.setTitle(R.string.core_option_news);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                intentSettings.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                this.startActivity(intentSettings);
                finish();
                return true;
            case R.id.action_info:
                Intent intentInfo = new Intent(this, InfoActivity.class);
                this.startActivity(intentInfo);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(CoreContract.Presenter presenter) {
        mCorePresenter = presenter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("onRequestPermission", "refreshLocationInCorePresenter");
                        mCorePresenter.refreshLocation();
                        Toast.makeText(getApplicationContext(), R.string.core_yes_location_toast, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("onRequestPermission", "Permission denied");
                    Toast.makeText(getApplicationContext(), R.string.core_no_location_toast, Toast.LENGTH_LONG).show();
                    mCorePresenter.refreshLocation();
                }
            }
        }
    }

    @Override
    public void onListFragmentInteraction(String item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(AstroEvent event) {

    }

    private BottomBarAdapter createBottomBarAdapter() {
        BottomBarAdapter pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        final TodayFragment todayFragment = new TodayFragment();
        final NewsFragment newsFragment = new NewsFragment();
        final CalendarFragment calendarFragment = new CalendarFragment();
        final EventsFragment eventsFragment = new EventsFragment();
        newsFragment.setPresenter(new NewsPresenter(newsFragment, getApplicationContext()));
        calendarFragment.setPresenter(new CalendarPresenter(calendarFragment));
        eventsFragment.setPresenter(new EventsPresenter(eventsFragment, getApplicationContext()));
        TodayPresenter presenter = new TodayPresenter(todayFragment, getApplicationContext());
        todayFragment.setPresenter(presenter);
        mCorePresenter = new CorePresenter(presenter);
        pagerAdapter.addFragments(calendarFragment);
        pagerAdapter.addFragments(todayFragment);
        pagerAdapter.addFragments(eventsFragment);
        pagerAdapter.addFragments(newsFragment);
        newsFragment.setBaseUrlForLanguage();
        return pagerAdapter;
    }
}
