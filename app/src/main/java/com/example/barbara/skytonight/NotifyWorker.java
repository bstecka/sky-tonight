package com.example.barbara.skytonight;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.example.barbara.skytonight.data.EventsDataSource;
import com.example.barbara.skytonight.data.repository.EventsRepository;
import com.example.barbara.skytonight.data.remote.LunarEclipseRemoteDataSource;
import com.example.barbara.skytonight.data.remote.MeteorShowerRemoteDataSource;
import com.example.barbara.skytonight.data.remote.SolarEclipseRemoteDataSource;
import com.example.barbara.skytonight.entity.AstroEvent;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.entity.MeteorShowerEvent;
import com.example.barbara.skytonight.entity.SolarEclipseEvent;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotifyWorker extends Worker {

    private Context context;
    private final EventsRepository mEventsRepository;

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        this.mEventsRepository = EventsRepository.getInstance(
                SolarEclipseRemoteDataSource.getInstance(context),
                LunarEclipseRemoteDataSource.getInstance(context),
                MeteorShowerRemoteDataSource.getInstance(context));
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getBoolean(AppConstants.PREF_KEY_NOTIF_EVENTS, true)) {
            triggerNotificationsForEvents();
        }
        return Worker.Result.SUCCESS;
    }

    private void triggerNotificationsForEvents() {
        mEventsRepository.getEvents(0, 0, new EventsDataSource.GetEventsCallback() {
            @Override
            public void onDataLoaded(List<AstroEvent> events) {
                processEventList(events);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("NotifyWorker", "Data not available");
            }
        });
    }

    private void processEventList(List<AstroEvent> events) {
        Resources res = context.getResources();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(LocaleHelper.getPersistedLocale(context));
        res.updateConfiguration(conf, null);
        long timeWindowMilis = 60 * 1000 * 12;
        long timeCushion = 60 * 1000;
        Calendar now = Calendar.getInstance();
        for (int i = 0; i < events.size(); i++) {
            AstroEvent event = events.get(i);
            long diff = event.getPeakDate().getTime() - now.getTime().getTime();
            if (diff > (-1 * timeCushion) && diff < timeWindowMilis + timeCushion) {
                final SimpleDateFormat sdfShort = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
                final SimpleDateFormat sdfLong = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.getDefault());
                if (event instanceof MeteorShowerEvent) {
                    int stringId = context.getResources().getIdentifier(event.getName(), "string", context.getPackageName());
                    String peakDateStr = sdfShort.format(event.getPeakDate());
                    @SuppressLint("StringFormatMatches")
                    String message = context.getString(R.string.notif_msg_event_meteor, event.getLongName(), context.getString(stringId), peakDateStr);
                    makeStatusNotification(context.getResources().getString(R.string.notif_title_event), message);
                }
                else if (event instanceof LunarEclipseEvent) {
                    String startDateStr = sdfLong.format(event.getStartDate());
                    String peakDateStr = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getPeakDate());
                    String message = context.getString(R.string.notif_msg_event_lunar, event.getLongName(), startDateStr, peakDateStr);
                    makeStatusNotification(context.getResources().getString(R.string.notif_title_event), message);
                }
                else if (event instanceof SolarEclipseEvent) {
                    String peakDateStr = sdfLong.format(event.getPeakDate());
                    String message = context.getString(R.string.notif_msg_event_solar, event.getLongName(), peakDateStr);
                    makeStatusNotification(context.getResources().getString(R.string.notif_title_event), message);
                }
            }
        }
    }

    private void makeStatusNotification(String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = AppConstants.VERBOSE_NOTIFICATION_CHANNEL_NAME;
            String description = AppConstants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(AppConstants.CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);
        NotificationManagerCompat.from(context).notify(AppConstants.NOTIFICATION_ID, builder.build());
    }
}