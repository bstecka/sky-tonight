package com.example.barbara.skytonight.presentation.core;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.barbara.skytonight.data.RepositoryFactory;
import com.example.barbara.skytonight.data.repository.LocationRepository;
import com.example.barbara.skytonight.entity.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.LocationDataSource;
import com.example.barbara.skytonight.data.repository.AstroObjectRepository;
import com.example.barbara.skytonight.data.ISSDataSource;
import com.example.barbara.skytonight.entity.ISSObject;
import com.example.barbara.skytonight.data.repository.ISSRepository;
import com.example.barbara.skytonight.data.WeatherDataSource;
import com.example.barbara.skytonight.entity.WeatherObject;
import com.example.barbara.skytonight.data.repository.WeatherRepository;
import com.example.barbara.skytonight.entity.AstroConstants;
import java.util.Calendar;
import java.util.List;

public class TodayPresenter implements TodayContract.Presenter {

    private final AstroObjectRepository mAstroObjectRepository;
    private final LocationRepository mLocationRepository;
    private final WeatherRepository mWeatherRepository;
    private final ISSRepository mISSRepository;
    private final TodayContract.View mTodayView;

    public TodayPresenter(TodayContract.View mTodayView, Context context) {
        this.mTodayView = mTodayView;
        this.mAstroObjectRepository = RepositoryFactory.getAstroObjectRepository(context);
        this.mLocationRepository = RepositoryFactory.getLocationRepository();
        this.mWeatherRepository = RepositoryFactory.getWeatherRepository(context);
        this.mISSRepository = RepositoryFactory.getISSRepository(context);
    }

    @Override
    public void start() {
        mLocationRepository.getUserLocation(mTodayView.getCurrentActivity(), new LocationDataSource.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                Log.e("TodayPresenter", "onDataLoaded mFusedLocationClient success " + location.getLatitude() + " " + location.getLongitude());
                mTodayView.refreshLocationInAdapter(location);
                loadISS(location);
                loadWeather(location);
                showObjects();
            }

            @Override
            public void onRequestForPermission() {
                Log.e("TodayPresenter", "Waiting for response to request for permission @ CoreActivity");
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("TodayPresenter", "onDataNotAvailable mFusedLocationClient failure");
                showObjects();
            }
        });
    }

    private void loadISS(Location location){
        final Calendar time = Calendar.getInstance();
        int overhead = mTodayView.getTimeOverhead();
        time.add(Calendar.HOUR, overhead);
        mISSRepository.getISSObject(time, location.getLatitude(), location.getLongitude(), new ISSDataSource.GetISSObject() {
            @Override
            public void onDataLoaded(ISSObject issObject) {
                mTodayView.updateList(issObject);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("TodayPresenter", "ISS station not available");
            }
        });
    }

    private void loadWeather(Location location){
        final Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR, mTodayView.getTimeOverhead());
        mWeatherRepository.getWeatherObjects(location.getLatitude(), location.getLongitude(), new WeatherDataSource.GetWeatherObjectsCallback() {
            @Override
            public void onDataLoaded(List<WeatherObject> weatherObjectList) {
                int index = -1;
                for (int i = 0; i < weatherObjectList.size() && index == -1; i++){
                    if (weatherObjectList.get(i).getTime().getTimeInMillis() >= time.getTimeInMillis())
                        index = i;
                }
                WeatherObject next = weatherObjectList.get(index);
                mTodayView.updateWeatherView(next);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("TodayPresenter", "Weather not available");
            }
        });
    }

    private void showObjects(){
        final Calendar time = Calendar.getInstance();
        int overhead = mTodayView.getTimeOverhead();
        time.add(Calendar.HOUR, overhead);
        int [] objectIds = AstroConstants.ASTRO_OBJECT_IDS;
        mTodayView.clearList();
        for (int id: objectIds){
            mAstroObjectRepository.getAstroObject(time, id, new AstroObjectsDataSource.GetAstroObjectsCallback() {
                @Override
                public void onDataLoaded(AstroObject object) {
                    mTodayView.updateList(object);
                }
                @Override
                public void onDataNotAvailable() {
                    Log.e("TodayPresenter", "AstroObject not available");
                }
            });
        }
    }
}
