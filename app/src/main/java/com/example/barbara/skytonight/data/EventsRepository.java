package com.example.barbara.skytonight.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EventsRepository implements EventsDataSource {

    private static EventsRepository INSTANCE = null;
    private SolarEclipseDataSource mSolarEclipseDataSource;

    public EventsRepository(SolarEclipseDataSource solarEclipseDataSource) {
        mSolarEclipseDataSource = solarEclipseDataSource;
    }

    public static EventsRepository getInstance(SolarEclipseDataSource solarEclipseDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EventsRepository(solarEclipseDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private void getEventsFromRemoteRepository(final double latitude, final double longitude, final GetEventsCallback callback) {
        final List<AstroEvent> eventList = new ArrayList<>();
        Log.e("EventsRepository", "getSolarEclipses");
        mSolarEclipseDataSource.getSolarEclipses(latitude, longitude, new SolarEclipseDataSource.GetSolarEclipsesCallback() {
            @Override
            public void onDataLoaded(List<SolarEclipseEvent> events) {
                eventList.addAll(events);
                callback.onDataLoaded(eventList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getEvents(final double latitude, final double longitude, GetEventsCallback callback) {
        getEventsFromRemoteRepository(latitude, longitude, callback);
    }
}
