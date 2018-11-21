package com.example.barbara.skytonight.presentation.details;
import android.util.Log;

import com.example.barbara.skytonight.data.MoonSunDataSource;
import com.example.barbara.skytonight.data.remote.MoonSunRemoteDataSource;
import com.example.barbara.skytonight.data.repository.MoonSunDataRepository;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.entity.MoonSunData;

import java.util.Calendar;

public class LunarDetailsPresenter implements LunarDetailsContract.Presenter {

    private final LunarDetailsContract.View view;
    private final MoonSunDataRepository moonSunDataRepository;
    private LunarEclipseEvent event;

    public LunarDetailsPresenter(LunarDetailsContract.View view, LunarEclipseEvent event) {
        this.view = view;
        this.moonSunDataRepository = MoonSunDataRepository.getInstance(MoonSunRemoteDataSource.getInstance(view.getContext()));
        this.event = event;
    }

    @Override
    public void start() {
        if (event != null) {
            Calendar time = event.getPeak();
            double latitude = event.getLatitude();
            double longitude = event.getLongitude();
            moonSunDataRepository.getMoonSunData(time, latitude, longitude, new MoonSunDataSource.GetMoonSunDataCallback(){
                @Override
                public void onDataLoaded(MoonSunData moonSunData) {
                    Log.e("Presenter", moonSunData.toString());
                    Log.e("Presenter", event.toString());
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }
}
