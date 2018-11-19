package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.barbara.skytonight.entity.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.VolleySingleton;
import com.example.barbara.skytonight.AppConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AstroObjectsRemoteDataSource implements AstroObjectsDataSource {

    private static AstroObjectsRemoteDataSource INSTANCE;
    private RequestQueue queue;

    private AstroObjectsRemoteDataSource() {}

    private AstroObjectsRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static AstroObjectsRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AstroObjectsRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAstroObject(final Calendar time, final int objectId, final GetAstroObjectsCallback callback) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UT1"));
        String startDate = isoFormat.format(time.getTime());
        Calendar time2 = Calendar.getInstance();
        time2.setTime(time.getTime());
        time2.add(Calendar.HOUR, 1);
        String endDate = isoFormat.format(time2.getTime());
        String url = String.format(AppConstants.ASTRO_OBJECT_API_URL, objectId, startDate, endDate);
        //String url = "https://ssd.jpl.nasa.gov/horizons_batch.cgi?batch=1&COMMAND='" + objectId + "'&MAKE_EPHEM='YES'&TABLE_TYPE='OBSERVER'&START_TIME='" + str_date + "'&STOP_TIME='" + str_date_end + "'&STEP_SIZE='30m'&CSV_FORMAT='YES'";
        //Log.e("RemoteDataSource", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AstroObject object = processString(response, objectId, time);
                        callback.onDataLoaded(object);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDataNotAvailable();
            }
        });
        queue.add(stringRequest);
        //Log.e("RemoteDataSource", url + " request added to queue");
    }

    private AstroObject processString(String str, int objectId, Calendar time) {
        AstroObject astroObject = new AstroObject();
        Boolean waxing = false;
        BufferedReader in = new BufferedReader(new StringReader(str));
        try {
            in.readLine();
            String inputLine = in.readLine(), objectName = "";
            Pattern pattern = Pattern.compile("(\\d{4})");
            Matcher matcher = pattern.matcher(inputLine);
            while (matcher.find())
                objectName = inputLine.substring(matcher.end(), inputLine.length()-10).trim();
            while ((inputLine = in.readLine()) != null && !inputLine.equals("$$SOE"));
            StringBuilder content = new StringBuilder();
            if ((inputLine = in.readLine()) != null && !inputLine.equals("$$EOE"))
                content.append(inputLine);
            List<String> splitList = Arrays.asList(content.toString().split(","));
            String RA = splitList.get(3), decl = splitList.get(4);
            double illu = Double.parseDouble(splitList.get(21).trim());
            if ((inputLine = in.readLine()) != null && !inputLine.equals("$$EOE")) {
                splitList = Arrays.asList(inputLine.split(","));
                String laterIllu = splitList.get(21).trim();
                if (Double.parseDouble(laterIllu) - illu > 0)
                    waxing = true;
            }
            astroObject = new AstroObject(objectId, objectName, rightAscToDeg(RA), strToDeg(decl), illu, waxing, time);
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
