package com.example.barbara.skytonight.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.barbara.skytonight.NotifyWorker;
import com.example.barbara.skytonight.NotifyWorkerISS;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.presentation.core.CoreActivity;
import com.example.barbara.skytonight.AppConstants;
import com.example.barbara.skytonight.presentation.util.MyContextWrapper;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import static androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, AppConstants.LANG_EN));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WorkManager.getInstance().cancelAllWork();
        runNotifyWorker();
        runNotifyWorkerISS();
        Intent myIntent = new Intent(MainActivity.this, CoreActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    private void runNotifyWorker() {
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(NotifyWorker.class, 1000 * 60 * 6, TimeUnit.MILLISECONDS);
        builder.setConstraints(Constraints.NONE).addTag("event");
        PeriodicWorkRequest worker = builder.build();
        Log.e("runNotifyWorker", worker.toString());
        WorkManager.getInstance().enqueue(worker);
    }

    private void runNotifyWorkerISS() {
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(NotifyWorkerISS.class, MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
        builder.setConstraints(Constraints.NONE).addTag("iss");
        PeriodicWorkRequest worker = builder.build();
        Log.e("runNotifyWorkerISS", worker.toString());
        WorkManager.getInstance().enqueue(worker);
    }
}
