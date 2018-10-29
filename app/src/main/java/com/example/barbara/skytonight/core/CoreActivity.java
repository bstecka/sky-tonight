package com.example.barbara.skytonight.core;

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
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.AstroEvent;
import com.example.barbara.skytonight.data.CoreRepository;
import com.example.barbara.skytonight.data.EventsRepository;
import com.example.barbara.skytonight.data.AstroObjectRepository;
import com.example.barbara.skytonight.data.ISSDataSource;
import com.example.barbara.skytonight.data.ISSRepository;
import com.example.barbara.skytonight.data.NewsRepository;
import com.example.barbara.skytonight.data.WeatherRepository;
import com.example.barbara.skytonight.data.remote.ArticleFetchService;
import com.example.barbara.skytonight.data.remote.AstroObjectsRemoteDataSource;
import com.example.barbara.skytonight.data.remote.ISSRemoteDataSource;
import com.example.barbara.skytonight.data.remote.LunarEclipseRemoteDataSource;
import com.example.barbara.skytonight.data.remote.MeteorShowerRemoteDataSource;
import com.example.barbara.skytonight.data.remote.NewsRemoteDataSource;
import com.example.barbara.skytonight.data.remote.SolarEclipseRemoteDataSource;
import com.example.barbara.skytonight.data.remote.WeatherRemoteDataSource;
import com.example.barbara.skytonight.settings.SettingsActivity;
import com.example.barbara.skytonight.util.AppConstants;
import com.example.barbara.skytonight.util.LocaleHelper;
import com.example.barbara.skytonight.util.MyContextWrapper;

public class CoreActivity extends AppCompatActivity implements CoreContract.View,
        TodayFragment.OnListFragmentInteractionListener, CalendarFragment.OnFragmentInteractionListener,
        NewsFragment.OnListFragmentInteractionListener, EventsFragment.OnListFragmentInteractionListener {

    private CoreContract.Presenter mCorePresenter;
    private BottomNavigationView bottomNavigationView;
    private MyViewPager viewPager;
    private BottomBarAdapter pagerAdapter;

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
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        final TodayFragment todayFragment = new TodayFragment();
        final NewsFragment newsFragment = new NewsFragment();
        NewsRepository newsRepository = NewsRepository.getInstance(NewsRemoteDataSource.getInstance(this), new ArticleFetchService(this));
        newsRepository.setBaseUrl(getBaseUrlForLanguage());
        newsFragment.setPresenter(new NewsPresenter(newsRepository, newsFragment));
        final CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setPresenter(new CalendarPresenter(calendarFragment));
        final EventsFragment eventsFragment = new EventsFragment();
        final TodayPresenter presenter = new TodayPresenter(AstroObjectRepository.getInstance(AstroObjectsRemoteDataSource.getInstance(this)), CoreRepository.getInstance(), WeatherRepository.getInstance(WeatherRemoteDataSource.getInstance(this)), ISSRepository.getInstance(ISSRemoteDataSource.getInstance(this)), todayFragment);
        todayFragment.setPresenter(presenter);
        mCorePresenter = new CorePresenter(presenter);
        EventsPresenter eventsPresenter = new EventsPresenter(CoreRepository.getInstance(), EventsRepository.getInstance(SolarEclipseRemoteDataSource.getInstance(this), LunarEclipseRemoteDataSource.getInstance(this), MeteorShowerRemoteDataSource.getInstance(this)), eventsFragment);
        eventsFragment.setPresenter(eventsPresenter);
        pagerAdapter.addFragments(calendarFragment);
        pagerAdapter.addFragments(todayFragment);
        pagerAdapter.addFragments(eventsFragment);
        pagerAdapter.addFragments(newsFragment);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        toolbar.setTitle(R.string.core_option_today);
        bottomNavigationView = findViewById(R.id.bottom_nav);
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

    private String getBaseUrlForLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = preferences.getString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_EN);
        if (language.equals(AppConstants.LANG_PL))
            return AppConstants.NEWS_URL_PL;
        else
            return AppConstants.NEWS_URL_EN;
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
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                this.startActivity(intent);
                finish();
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
}
