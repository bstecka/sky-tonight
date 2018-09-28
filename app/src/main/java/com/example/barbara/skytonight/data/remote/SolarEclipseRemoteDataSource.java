package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.SolarEclipseDataSource;
import com.example.barbara.skytonight.data.SolarEclipseEvent;
import com.example.barbara.skytonight.data.VolleySingleton;
import com.example.barbara.skytonight.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolarEclipseRemoteDataSource implements SolarEclipseDataSource {

    private static SolarEclipseRemoteDataSource INSTANCE;
    private RequestQueue queue;
    private final String url = AppConstants.API_URL + "solar-eclipses?future=1";

    private SolarEclipseRemoteDataSource() {}

    private SolarEclipseRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static SolarEclipseRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SolarEclipseRemoteDataSource(context);
        }
        return INSTANCE;
    }

    public void getSolarEclipses(final double latitude, final double longitude, int month, int year, final GetSolarEclipsesCallback callback) {
        final List<SolarEclipseEvent> events = new ArrayList<>();
        String url = this.url + "&month=" + month + "&year=" + year;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("events");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        int id = object.getInt("id");
                        int type = object.getInt("eclipse_type");
                        Calendar cal = Calendar.getInstance();
                        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("date"));
                        cal.setTime(date);
                        SolarEclipseEvent eclipseEvent = new SolarEclipseEvent(id, cal, type);
                        events.add(eclipseEvent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    Log.e("RemoteDataSource", "ParseException");
                }
                callback.onDataLoaded(events);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RemoteDataSource", error.toString());
                callback.onDataNotAvailable();
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void getSolarEclipses(final double latitude, final double longitude, final GetSolarEclipsesCallback callback) {
        final List<SolarEclipseEvent> events = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("events");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        int id = object.getInt("id");
                        int type = object.getInt("eclipse_type");
                        Calendar cal = Calendar.getInstance();
                        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("date"));
                        cal.setTime(date);
                        SolarEclipseEvent eclipseEvent = new SolarEclipseEvent(id, cal, type);
                        events.add(eclipseEvent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    Log.e("RemoteDataSource", "ParseException");
                }
                callback.onDataLoaded(events);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RemoteDataSource", error.toString());
                callback.onDataNotAvailable();
            }
        });
        queue.add(jsonObjectRequest);
    }
}