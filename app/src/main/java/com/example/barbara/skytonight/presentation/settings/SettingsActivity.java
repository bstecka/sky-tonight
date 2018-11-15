package com.example.barbara.skytonight.presentation.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.presentation.core.CoreActivity;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

public class SettingsActivity extends AppCompatActivity {

    SettingsFragment settingsFragment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.settingsFragment);
        settingsFragment = (SettingsFragment) currentFragment;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CoreActivity.class);
        startActivity(intent);
    }
}
