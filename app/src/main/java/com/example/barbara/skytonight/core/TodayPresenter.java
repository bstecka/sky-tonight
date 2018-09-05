package com.example.barbara.skytonight.core;

import android.util.Log;

import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.TodayRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TodayPresenter implements TodayContract.Presenter{

    private final TodayRepository mTodayRepository;
    private final TodayContract.View mTodayView;

    public TodayPresenter(TodayRepository mTodayRepository, TodayContract.View mTodayView) {
        this.mTodayRepository = mTodayRepository;
        this.mTodayView = mTodayView;
    }

    @Override
    public void start() {
        showObjects();
    }

    private void showObjects(){
        Calendar time = Calendar.getInstance();
        ArrayList<AstroObject> objects = new ArrayList<>();
        objects.addAll(mTodayRepository.getAstroObjects(time, new AstroObjectsDataSource.GetAstroObjectsCallback() {
            @Override
            public void onDataLoaded(String response, int objectId) {
                Log.e("Callback", "onDataLoaded");
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("Callback", "onDataLoaded");
            }
        }));
        mTodayView.updateList(objects);
    }

    AstroObject processString(String str, int objectId) {
        AstroObject astroObject = new AstroObject();
        BufferedReader in = new BufferedReader(new StringReader(str));
        try {
            String inputLine;
            StringBuilder content = new StringBuilder();
            in.readLine();
            String objectName = Arrays.asList(in.readLine().split(" ")).get(21);
            while ((inputLine = in.readLine()) != null && !inputLine.equals("$$SOE"));
            if ((inputLine = in.readLine()) != null && !inputLine.equals("$$EOE"))
                content.append(inputLine);
            List<String> splitList = Arrays.asList(content.toString().split(","));
            String RA = splitList.get(3);
            String decl = splitList.get(4);
            astroObject = new AstroObject(objectId, objectName, rightAscToDeg(RA), strToDeg(decl));
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
