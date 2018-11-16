package com.example.barbara.skytonight;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.barbara.skytonight.data.CoreDataSource;
import com.example.barbara.skytonight.data.CoreRepository;
import com.example.barbara.skytonight.data.ISSDataSource;
import com.example.barbara.skytonight.data.ISSRepository;
import com.example.barbara.skytonight.data.remote.ISSRemoteDataSource;
import com.example.barbara.skytonight.entity.ISSObject;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;
import static com.example.barbara.skytonight.Constants.CHANNEL_ID;

public class NotifyWorkerISS extends Worker {

    private Context context;
    private final CoreRepository mCoreRepository;
    private final ISSRepository mISSRepository;

    public NotifyWorkerISS(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        this.mCoreRepository = CoreRepository.getInstance();
        this.mISSRepository = ISSRepository.getInstance(ISSRemoteDataSource.getInstance(context));
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getBoolean(AppConstants.PREF_KEY_NOTIF_ISS, true)) {
            triggerNotificationsForISS();
            Log.e("NotifyWorker2", "N2 doing work");
        }
        //makeStatusNotification("Test", "ISS is not here");
        return Result.SUCCESS;
    }

    private void triggerNotificationsForISS() {
        mCoreRepository.getUserLocation(context, new CoreDataSource.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                mISSRepository.getISSObject(Calendar.getInstance(), location.getLatitude(), location.getLongitude(), new ISSDataSource.GetISSObject() {
                    @Override
                    public void onDataLoaded(ISSObject issObject) {
                        processISSObject(issObject);
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
            }

            @Override
            public void onRequestForPermission() {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void processISSObject(ISSObject issObject) {
        Resources res = context.getResources();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(LocaleHelper.getPersistedLocale(context));
        res.updateConfiguration(conf, null);
        long timeWindowMilis = MIN_PERIODIC_INTERVAL_MILLIS; //NOTIFY 15 MINUTES BEFORE THE EVENT
        long timeCushion = 60 * 1000 * 10; // 10 MINUTES CUSHION
        Calendar now = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        long diff = issObject.getNextFlyby().getTime().getTime() - now.getTime().getTime();
        if (diff > (-1 * timeCushion) && diff < (timeWindowMilis + timeCushion)) {
            String message = context.getString(R.string.notif_msg_iss, sdf.format(issObject.getNextFlyby().getTime()));
            makeStatusNotification(context.getResources().getString(R.string.notif_title_iss), message);
        }
    }

    private void makeStatusNotification(String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME;
            String description = Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);
        NotificationManagerCompat.from(context).notify(Constants.NOTIFICATION_ID, builder.build());
    }
}