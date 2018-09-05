package com.example.barbara.skytonight.core;

import android.util.Log;

import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.TodayRepository;
import com.example.barbara.skytonight.util.AstroConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodayPresenter implements TodayContract.Presenter{

    private final TodayRepository mTodayRepository;
    private final TodayContract.View mTodayView;

    public TodayPresenter(TodayRepository mTodayRepository, TodayContract.View mTodayView) {
        Log.e("Presenter", "Presenter hello");
        this.mTodayRepository = mTodayRepository;
        this.mTodayView = mTodayView;
    }

    @Override
    public void start() {
        showObjects();
    }

    private void showObjects(){
        Calendar time = Calendar.getInstance();
        int [] objectIds = AstroConstants.ASTRO_OBJECT_IDS;
        Log.e("Presenter", "showObjects");
        mTodayView.clearList();
        for (int i = 0; i < objectIds.length; i++) {
            mTodayRepository.getAstroObject(time, objectIds[i], new AstroObjectsDataSource.GetAstroObjectsCallback() {
                @Override
                public void onDataLoaded(String response, int objectId) {
                    Log.e("Presenter", "onDataLoaded");
                    AstroObject astroObject = processString(response, objectId);
                    mTodayView.updateList(astroObject);
                }
                @Override
                public void onDataNotAvailable() {
                    Log.e("Presenter", "onDataNotAvailable");
                }
            });
        }
    }

    AstroObject processString(String str, int objectId) {
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
