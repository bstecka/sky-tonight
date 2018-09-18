package com.example.barbara.skytonight.core;

import android.location.Location;

import com.example.barbara.skytonight.data.CoreDataSource;
import com.example.barbara.skytonight.data.CoreRepository;

public class EventsPresenter implements EventsContract.Presenter {

    private final CoreRepository mCoreRepository;
    private final EventsContract.View mEventsView;

    public EventsPresenter(CoreRepository mCoreRepository, EventsContract.View mEventsView) {
        this.mCoreRepository = mCoreRepository;
        this.mEventsView = mEventsView;
    }

    @Override
    public void getUserLocation(final EventsContract.GetUserLocationCallback callback) {
        mCoreRepository.getUserLocation(mEventsView.getCurrentActivity(), new CoreDataSource.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                callback.onDataLoaded(location);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void start() {

    }
}
