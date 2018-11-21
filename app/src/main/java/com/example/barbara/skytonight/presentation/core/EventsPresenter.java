package com.example.barbara.skytonight.presentation.core;

import android.location.Location;
import android.util.Log;

import com.example.barbara.skytonight.entity.AstroEvent;
import com.example.barbara.skytonight.data.CoreDataSource;
import com.example.barbara.skytonight.data.repository.CoreRepository;
import com.example.barbara.skytonight.data.EventsDataSource;
import com.example.barbara.skytonight.data.repository.EventsRepository;
import com.example.barbara.skytonight.AppConstants;

import java.util.ArrayList;
import java.util.Calendar;
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
    public void start() {
        showEventsForMonth(Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public void showEventsForMonth(final int month, final int year){
        mCoreRepository.getUserLocation(mEventsView.getCurrentActivity(), new CoreDataSource.GetUserLocationCallback()  {
            @Override
            public void onDataLoaded(Location location) {
                getEventsForMonth(location.getLatitude(), location.getLongitude(), month, year);
            }

            @Override
            public void onRequestForPermission() {
                Log.e("EventsPresenter", "Waiting for response to request for permission @ CoreActivity (NOOP)");
            }

            @Override
            public void onDataNotAvailable() {
                getEventsForMonth(AppConstants.DEFAULT_LATITUDE, AppConstants.DEFAULT_LONGITUDE, month, year);
            }
        });
    }

    private void getEventsForMonth(double latitude, double longitude, int month, int year) {
        mEventsView.clearList();
        mEventsRepository.getEvents(latitude, longitude, month, year, new EventsDataSource.GetEventsCallback() {
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
                Log.e("EventsPresenter", "DataNotAvailable");
            }
        });
    }

    /*private void getEvents(double latitude, double longitude) {
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
                Log.e("EventsPresenter", "DataNotAvailable");
            }
        });
    }*/
}
