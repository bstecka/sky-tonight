package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.util.AstroConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AstroObjectsRemoteDataSource implements AstroObjectsDataSource {

    private static AstroObjectsRemoteDataSource INSTANCE;
    private RequestQueue queue;

    private AstroObjectsRemoteDataSource() {}

    public AstroObjectsRemoteDataSource(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static AstroObjectsRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AstroObjectsRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAstroObject(Calendar time, final int objectId, final GetAstroObjectsCallback callback) {
        String RA, decl;
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UT1"));
        String str_date = isoFormat.format(time.getTime());
        System.out.println("Object " + objectId + ", " + str_date.replaceAll("%20", " "));
        Calendar time2 = Calendar.getInstance();
        time2.setTime(time.getTime());
        time2.add(Calendar.HOUR, 1);
        String str_date_end = isoFormat.format(time2.getTime());
        String url = "https://ssd.jpl.nasa.gov/horizons_batch.cgi?batch=1&COMMAND='" + objectId + "'&MAKE_EPHEM='YES'&TABLE_TYPE='OBSERVER'&START_TIME='" + str_date + "'&STOP_TIME='" + str_date_end + "'&STEP_SIZE='30m'&CSV_FORMAT='YES'";
        Log.e("RemoteDataSource", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onDataLoaded(response, objectId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDataNotAvailable();
            }
        });
        queue.add(stringRequest);
        Log.e("RemoteDataSource", "Request added to queue");
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

}
