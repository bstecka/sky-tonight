package com.example.barbara.skytonight.presentation.details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.entity.SolarEclipseEvent;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

import java.io.Serializable;

public class SolarDetailsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_details);
        SolarEclipseEvent event = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable("event");
            if (serializable instanceof SolarEclipseEvent)
                event = (SolarEclipseEvent) serializable;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.solarDetailsFragment);
        SolarDetailsFragment fragment = (SolarDetailsFragment) currentFragment;
        if (fragment == null) {
            fragment = new SolarDetailsFragment();
            fragment.setPresenter(new SolarDetailsPresenter(fragment, event));
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.solarDetailsFragment, fragment);
            transaction.commit();
        }
        else {
            fragment.setPresenter(new SolarDetailsPresenter(fragment, event));
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}
