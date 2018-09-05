package com.example.barbara.skytonight.core;

import android.app.Fragment;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.TodayRepository;
import com.example.barbara.skytonight.data.remote.AstroObjectsRemoteDataSource;

public class CoreActivity extends AppCompatActivity implements TodayFragment.OnListFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener {

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
        TodayFragment todayFragment = new TodayFragment();
        TodayPresenter presenter = new TodayPresenter(new TodayRepository(new AstroObjectsRemoteDataSource(this)),todayFragment);
        todayFragment.setPresenter(presenter);
        NewsFragment newsFragment = new NewsFragment();
        pagerAdapter.addFragments(todayFragment);
        pagerAdapter.addFragments(newsFragment);
        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottombaritem_calls:
                                viewPager.setCurrentItem(1);
                                return true;
                            case R.id.bottombaritem_recents:
                                viewPager.setCurrentItem(0);
                                return true;
                            case R.id.bottombaritem_trips:
                                viewPager.setCurrentItem(1);
                                return true;
                        }
                        return false;
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
