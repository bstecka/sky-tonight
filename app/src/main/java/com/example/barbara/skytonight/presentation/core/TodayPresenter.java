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

    public TodayPresenter(TodayContract.View mTodayView, AstroObjectRepository astroObjectRepository, LocationRepository locationRepository, WeatherRepository weatherRepository, ISSRepository issRepository) {
        this.mTodayView = mTodayView;
        this.mAstroObjectRepository = astroObjectRepository;
        this.mLocationRepository = locationRepository;
        this.mWeatherRepository = weatherRepository;
        this.mISSRepository = issRepository;
    }

    @Override
    public void start() {
        mLocationRepository.getUserLocation(mTodayView.getCurrentActivity(), new LocationDataSource.GetUserLocationCallback() {
            @Override
            public void onDataLoaded(Location location) {
                Log.e("TodayPresenter", "onDataLoaded mFusedLocationClient success " + location.getLatitude() + " " + location.getLongitude());
                mTodayView.refreshLocationInAdapter(location);
                loadISS(location.getLatitude(), location.getLongitude());
                loadWeather(location.getLatitude(), location.getLongitude());
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

    public void loadISS(double latitude, double longitude){
        final Calendar time = Calendar.getInstance();
        int overhead = mTodayView.getTimeOverhead();
        time.add(Calendar.HOUR, overhead);
        mISSRepository.getISSObject(time, latitude, longitude, new ISSDataSource.GetISSObjectCallback() {
            @Override
            public void onDataLoaded(ISSObject issObject) {
                mTodayView.hideErrorText();
                mTodayView.updateList(issObject);
            }

            @Override
            public void onDataNotAvailable() {
                mTodayView.showErrorText();
            }
        });
    }

    public void loadWeather(double latitude, double longitude){
        final Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR, mTodayView.getTimeOverhead());
        mWeatherRepository.getWeatherObjects(latitude, longitude, new WeatherDataSource.GetWeatherObjectsCallback() {
            @Override
            public void onDataLoaded(List<WeatherObject> weatherObjectList) {
                int index = -1;
                for (int i = 0; i < weatherObjectList.size() && index == -1; i++){
                    if (weatherObjectList.get(i).getTime().getTimeInMillis() >= time.getTimeInMillis())
                        index = i;
                }
                if (index != -1) {
                    WeatherObject next = weatherObjectList.get(index);
                    mTodayView.hideErrorText();
                    mTodayView.updateWeatherView(next);
                } else {
                    mTodayView.showErrorText();
                }
            }

            @Override
            public void onDataNotAvailable() {
                mTodayView.showErrorText();
            }
        });
    }

    public void showObjects(){
        final Calendar time = Calendar.getInstance();
        int overhead = mTodayView.getTimeOverhead();
        time.add(Calendar.HOUR, overhead);
        int [] objectIds = AstroConstants.ASTRO_OBJECT_IDS;
        mTodayView.clearList();
        for (int id: objectIds){
            mAstroObjectRepository.getAstroObject(time, id, new AstroObjectsDataSource.GetAstroObjectsCallback() {
                @Override
                public void onDataLoaded(AstroObject object) {
                    mTodayView.hideErrorText();
                    mTodayView.updateList(object);
                }
                @Override
                public void onDataNotAvailable() {
                    mTodayView.showErrorText();
                }
            });
        }
    }
}
