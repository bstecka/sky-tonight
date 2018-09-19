package com.example.barbara.skytonight.core;

import android.location.Location;
import android.util.Log;

import com.example.barbara.skytonight.data.AstroEvent;
import com.example.barbara.skytonight.data.CoreDataSource;
import com.example.barbara.skytonight.data.CoreRepository;
import com.example.barbara.skytonight.data.EventsDataSource;
import com.example.barbara.skytonight.data.EventsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventsPresenter implements EventsContract.Presenter {

    private final CoreRepository mCoreRepository;
    private final EventsRepository mEventsRepository;
    private final EventsContract.View mEventsView;

    public EventsPresenter(CoreRepository mCoreRepository, EventsRepository mEventsRepository, EventsContract.View mEventsView) {
        this.mCoreRepository = mCoreRepository;
        this.mEventsRepository = mEventsRepository;
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
        showEvents();
    }

    private void showEvents(){
        getUserLocation(new EventsContract.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                loadEvents(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onDataNotAvailable() {
                loadEvents(-27.104671, -109.360481);
            }
        });
    }

    private void loadEvents(double latitude, double longitude) {
        mEventsView.clearList();
        mEventsRepository.getEvents(latitude, longitude, new EventsDataSource.GetEventsCallback() {
            @Override
            public void onDataLoaded(List<AstroEvent> events) {
                ArrayList<AstroEvent> arrayList = (ArrayList<AstroEvent>) events;
                if (arrayList.size() > 0) {
                    Collections.sort(arrayList, new Comparator<AstroEvent>() {
                        @Override
                        public int compare(final AstroEvent object1, final AstroEvent object2) {
                            return object1.getStartDate().compareTo(object2.getStartDate());
                        }
                    });
                }
                mEventsView.updateList(arrayList);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("EventsPresenter", "onDataNotAvailable");
            }
        });
    }
}
