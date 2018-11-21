package com.example.barbara.skytonight.presentation.details;
import android.util.Log;

import com.example.barbara.skytonight.data.MoonSunDataSource;
import com.example.barbara.skytonight.data.remote.MoonSunRemoteDataSource;
import com.example.barbara.skytonight.data.repository.MoonSunDataRepository;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.entity.MoonSunData;
import com.example.barbara.skytonight.entity.SolarEclipseEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SolarDetailsPresenter implements SolarDetailsContract.Presenter {

    private final SolarDetailsContract.View view;
    private SolarEclipseEvent event;

    public SolarDetailsPresenter(SolarDetailsContract.View view, SolarEclipseEvent event) {
        this.view = view;
        this.event = event;
    }

    @Override
    public void start() {
        if (event != null) {
            Log.e("SolarPresenter", event.toString());
            setDataInView(event);
        }
    }

    private void setDataInView(SolarEclipseEvent event) {
        view.setTitle(event.getName(), new SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(event.getPeakDate()));
        view.setTimeLine(getStringForCalendar(event.getPeak()));
        view.setImage(event.getImageUrl());
    }

    private String getStringForCalendar(Calendar calendar) {
        if (calendar == null)
            return "";
        else
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
    }
}
