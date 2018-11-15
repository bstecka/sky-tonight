package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.skytonight.data.LunarEclipseDataSource;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
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

public class LunarEclipseRemoteDataSource implements LunarEclipseDataSource {

    private static LunarEclipseRemoteDataSource INSTANCE;
    private RequestQueue queue;
    private final String url = AppConstants.API_URL + "lunar-eclipses?future=1";

    private LunarEclipseRemoteDataSource() {}

    private LunarEclipseRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static LunarEclipseRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LunarEclipseRemoteDataSource(context);
        }
        return INSTANCE;
    }

    public void getLunarEclipses(final double latitude, final double longitude, int month, int year, final GetLunarEclipsesCallback callback) {
        final List<LunarEclipseEvent> events = new ArrayList<>();
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
                        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("greatest_eclipse"));
                        cal.setTime(date);
                        LunarEclipseEvent eclipseEvent = new LunarEclipseEvent(id, cal, type);
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
    public void getLunarEclipses(final double latitude, final double longitude, final GetLunarEclipsesCallback callback) {
        final List<LunarEclipseEvent> events = new ArrayList<>();
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
                        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).parse(object.getString("greatest_eclipse"));
                        cal.setTime(date);
                        LunarEclipseEvent eclipseEvent = new LunarEclipseEvent(id, cal, type);
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
