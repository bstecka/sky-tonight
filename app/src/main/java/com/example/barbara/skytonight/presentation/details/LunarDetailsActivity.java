package com.example.barbara.skytonight.presentation.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LunarDetailsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunar_details);
        LunarEclipseEvent event = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable("event");
            if (serializable instanceof LunarEclipseEvent)
                event = (LunarEclipseEvent) serializable;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.lunarDetailsFragment);
        LunarDetailsFragment fragment = (LunarDetailsFragment) currentFragment;
        if (fragment == null) {
            fragment = new LunarDetailsFragment();
            fragment.setPresenter(new LunarDetailsPresenter(fragment, event));
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.lunarDetailsFragment, fragment);
            transaction.commit();
        }
        else {
            fragment.setPresenter(new LunarDetailsPresenter(fragment, event));
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
