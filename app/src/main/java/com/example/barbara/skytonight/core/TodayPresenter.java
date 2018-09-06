package com.example.barbara.skytonight.core;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.TodayRepository;
import com.example.barbara.skytonight.util.AstroConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodayPresenter implements TodayContract.Presenter{

    private final TodayRepository mTodayRepository;
    private final TodayContract.View mTodayView;
    private Location userLocation;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 99;

    public TodayPresenter(TodayRepository mTodayRepository, TodayContract.View mTodayView) {
        Log.e("Presenter", "Presenter hello");
        this.mTodayRepository = mTodayRepository;
        this.mTodayView = mTodayView;
        this.userLocation = new Location("dummyprovider");
        this.userLocation.setLongitude(0.0);
        this.userLocation.setLatitude(0.0);
    }

    private void getUserLocation() {

    }

    @Override
    public void start() {
        showObjects();
    }

    private void showObjects(){
        final Calendar time = Calendar.getInstance();
        Log.e("Presenter", time.getTime().toString());
        int [] objectIds = AstroConstants.ASTRO_OBJECT_IDS;
        Log.e("Presenter", "showObjects");
        mTodayView.clearList();
        for (int i = 0; i < objectIds.length; i++) {
            mTodayRepository.getAstroObject(time, objectIds[i], new AstroObjectsDataSource.GetAstroObjectsCallback() {
                @Override
                public void onDataLoaded(String response, int objectId) {
                    Log.e("Presenter", "onDataLoaded");
                    Log.e("Presenter2", time.getTime().toString());
                    AstroObject astroObject = processString(response, objectId, time);
                    mTodayView.updateList(astroObject);
                }
                @Override
                public void onDataNotAvailable() {
                    Log.e("Presenter", "onDataNotAvailable");
                }
            });
        }
    }

    AstroObject processString(String str, int objectId, Calendar time) {
        AstroObject astroObject = new AstroObject();
        BufferedReader in = new BufferedReader(new StringReader(str));
        try {
            StringBuilder content = new StringBuilder();
            in.readLine();
            String inputLine = in.readLine(), objectName = "";
            Pattern pattern = Pattern.compile("(\\d{4})");
            Matcher matcher = pattern.matcher(inputLine);
            while (matcher.find())
                objectName = inputLine.substring(matcher.end(), inputLine.length()-10).trim();
            while ((inputLine = in.readLine()) != null && !inputLine.equals("$$SOE"));
            if ((inputLine = in.readLine()) != null && !inputLine.equals("$$EOE"))
                content.append(inputLine);
            List<String> splitList = Arrays.asList(content.toString().split(","));
            String RA = splitList.get(3);
            String decl = splitList.get(4);
            Log.e("Presenter3", time.getTime().toString());
            astroObject = new AstroObject(objectId, objectName, rightAscToDeg(RA), strToDeg(decl), time);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return astroObject;
    }

    private double strToDeg(String str) {
        List<String> splitDegrees = Arrays.asList(str.split(" "));
        double hours, minutes, seconds;
        hours = Double.parseDouble(splitDegrees.get(0));
        minutes = Double.parseDouble(splitDegrees.get(1))/60.0;
        seconds = Double.parseDouble(splitDegrees.get(2))/3600.0;
        if (hours >= 0)
            return hours + Math.abs(minutes) + Math.abs(seconds);
        else
            return hours - Math.abs(minutes) - Math.abs(seconds);
    }

    private double rightAscToDeg(String str) {
        return strToDeg(str) * 15;
    }
}
