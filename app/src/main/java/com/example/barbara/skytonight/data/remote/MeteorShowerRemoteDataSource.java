package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.skytonight.data.MeteorShowerDataSource;
import com.example.barbara.skytonight.entity.MeteorShowerEvent;
import com.example.barbara.skytonight.data.VolleySingleton;
import com.example.barbara.skytonight.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class MeteorShowerRemoteDataSource implements MeteorShowerDataSource {

    private static MeteorShowerRemoteDataSource INSTANCE;
    private RequestQueue queue;
    private final String url = AppConstants.API_URL + "meteor-showers?future=1";

    private MeteorShowerRemoteDataSource() {}

    private MeteorShowerRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static MeteorShowerRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MeteorShowerRemoteDataSource(context);
        }
        return INSTANCE;
    }

    public void getMeteorShowers(final double latitude, final double longitude, int month, int year, final GetMeteorShowersCallback callback) {
        final List<MeteorShowerEvent> events = new ArrayList<>();
        String url = this.url + "&month=" + month + "&year=" + year;
        Log.e("getMeteorShowers " + latitude + " " + longitude, url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("events");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("name");
                        String radiant = object.getString("radiant");
                        double RA = (parseInt(radiant.substring(0,2)) + (double)parseInt(radiant.substring(3,5))/60) * 15;
                        double decl = parseDouble(radiant.substring(6).replace("°",""));
                        int zhr = object.getInt("zhr");
                        Calendar startCal = Calendar.getInstance();
                        Date startDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("start_date"));
                        startCal.setTime(startDate);
                        Calendar endCal = Calendar.getInstance();
                        Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("end_date"));
                        endCal.setTime(endDate);
                        Calendar peakCal = Calendar.getInstance();
                        Date peakDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("start_peak"));
                        peakCal.setTime(peakDate);
                        MeteorShowerEvent event = new MeteorShowerEvent(id, name, startCal, endCal, peakCal, zhr, RA, decl);
                        event.calculateVisibility(latitude, longitude);
                        events.add(event);
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
    public void getMeteorShowers(final double latitude, final double longitude, final GetMeteorShowersCallback callback) {
        final List<MeteorShowerEvent> events = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("events");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("name");
                        Calendar startCal = Calendar.getInstance();
                        Date startDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("start_date"));
                        startCal.setTime(startDate);
                        Calendar endCal = Calendar.getInstance();
                        Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("end_date"));
                        endCal.setTime(endDate);
                        Calendar peakCal = Calendar.getInstance();
                        Date peakDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("start_peak"));
                        peakCal.setTime(peakDate);
                        MeteorShowerEvent event = new MeteorShowerEvent(id, name, startCal, endCal, peakCal);
                        events.add(event);
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
