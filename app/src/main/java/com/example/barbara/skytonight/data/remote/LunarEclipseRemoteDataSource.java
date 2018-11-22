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
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
        String url = this.url + "&month=" + month + "&year=" + year;
        getLunarEclipses(url, latitude, longitude, callback);
    }

    @Override
    public void getLunarEclipses(final double latitude, final double longitude, final GetLunarEclipsesCallback callback) {
        getLunarEclipses(url, latitude, longitude, callback);
    }

    private void getLunarEclipses(String url, final double latitude, final double longitude, final GetLunarEclipsesCallback callback) {
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
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
                        sdf.setTimeZone(TimeZone.getTimeZone("UT"));
                        Calendar greatestEclipse = Calendar.getInstance();
                        greatestEclipse.setTime(sdf.parse(object.getString("greatest_eclipse")));
                        Calendar partialBegins = null, partialEnds = null;
                        if (object.getString("partial_begins").length() > 1) {
                            partialBegins = Calendar.getInstance();
                            partialBegins.setTime(sdf.parse(object.getString("partial_begins")));
                            partialEnds = Calendar.getInstance();
                            partialEnds.setTime(sdf.parse(object.getString("partial_ends")));
                        }
                        Calendar totalBegins = null, totalEnds = null;
                        if (object.getString("total_begins").length() > 1) {
                            totalBegins = Calendar.getInstance();
                            totalBegins.setTime(sdf.parse(object.getString("total_begins")));
                            totalEnds = Calendar.getInstance();
                            totalEnds.setTime(sdf.parse(object.getString("total_ends")));
                        }
                        Calendar penunmbralBegins = null, penunmbralEnds = null;
                        if (object.getString("penunmbral_begins").length() > 1) {
                            penunmbralBegins = Calendar.getInstance();
                            penunmbralBegins.setTime(sdf.parse(object.getString("penunmbral_begins")));
                            penunmbralEnds = Calendar.getInstance();
                            penunmbralEnds.setTime(sdf.parse(object.getString("penunmbral_ends")));
                        }
                        LunarEclipseEvent eclipseEvent = new LunarEclipseEvent(id, greatestEclipse, type, latitude, longitude,
                                partialBegins, partialEnds, penunmbralBegins, penunmbralEnds, totalBegins, totalEnds);
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
