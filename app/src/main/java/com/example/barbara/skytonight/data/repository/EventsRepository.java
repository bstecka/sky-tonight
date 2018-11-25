package com.example.barbara.skytonight.data.repository;

import android.util.Log;

import com.example.barbara.skytonight.data.EventsDataSource;
import com.example.barbara.skytonight.data.LunarEclipseDataSource;
import com.example.barbara.skytonight.data.MeteorShowerDataSource;
import com.example.barbara.skytonight.data.SolarEclipseDataSource;
import com.example.barbara.skytonight.entity.AstroEvent;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.entity.MeteorShowerEvent;
import com.example.barbara.skytonight.entity.SolarEclipseEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsRepository implements EventsDataSource {

    private static EventsRepository INSTANCE = null;
    private SolarEclipseDataSource mSolarEclipseDataSource;
    private LunarEclipseDataSource mLunarEclipseDataSource;
    private MeteorShowerDataSource mMeteorShowerDataSource;

    private EventsRepository(SolarEclipseDataSource solarEclipseDataSource, LunarEclipseDataSource lunarEclipseDataSource, MeteorShowerDataSource meteorShowerDataSource) {
        mSolarEclipseDataSource = solarEclipseDataSource;
        mLunarEclipseDataSource = lunarEclipseDataSource;
        mMeteorShowerDataSource = meteorShowerDataSource;
    }

    public static EventsRepository getInstance(SolarEclipseDataSource solarEclipseDataSource, LunarEclipseDataSource lunarEclipseDataSource, MeteorShowerDataSource meteorShowerDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EventsRepository(solarEclipseDataSource, lunarEclipseDataSource, meteorShowerDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getEvents(final double latitude, final double longitude, int month, int year, final GetEventsCallback callback) {
        final List<AstroEvent> eventList = new ArrayList<>();
        mSolarEclipseDataSource.getSolarEclipses(latitude, longitude, month, year, new SolarEclipseDataSource.GetSolarEclipsesCallback() {
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
        mLunarEclipseDataSource.getLunarEclipses(latitude, longitude, month, year, new LunarEclipseDataSource.GetLunarEclipsesCallback() {
            @Override
            public void onDataLoaded(List<LunarEclipseEvent> events) {
                eventList.addAll(events);
                callback.onDataLoaded(eventList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
        mMeteorShowerDataSource.getMeteorShowers(latitude, longitude, month, year, new MeteorShowerDataSource.GetMeteorShowersCallback() {
            @Override
            public void onDataLoaded(List<MeteorShowerEvent> events) {
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
    public void getEvents(final double latitude, final double longitude, final GetEventsCallback callback) {
        final List<AstroEvent> eventList = new ArrayList<>();
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
        mLunarEclipseDataSource.getLunarEclipses(latitude, longitude, new LunarEclipseDataSource.GetLunarEclipsesCallback() {
            @Override
            public void onDataLoaded(List<LunarEclipseEvent> events) {
                eventList.addAll(events);
                callback.onDataLoaded(eventList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
        mMeteorShowerDataSource.getMeteorShowers(latitude, longitude, new MeteorShowerDataSource.GetMeteorShowersCallback() {
            @Override
            public void onDataLoaded(List<MeteorShowerEvent> events) {
                eventList.addAll(events);
                callback.onDataLoaded(eventList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
