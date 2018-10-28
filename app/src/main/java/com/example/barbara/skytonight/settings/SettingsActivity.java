package com.example.barbara.skytonight.settings;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.barbara.skytonight.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.settingsFragment);
        SettingsFragment settingsFragment = (SettingsFragment) currentFragment;
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
            settingsFragment.setPresenter(new SettingsPresenter(settingsFragment));
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.articleFragment, settingsFragment);
            transaction.commit();
        } else {
            settingsFragment.setPresenter(new SettingsPresenter(settingsFragment));
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
