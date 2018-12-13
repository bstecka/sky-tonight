package com.example.barbara.skytonight.data.remote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.skytonight.data.ISSDataSource;
import com.example.barbara.skytonight.entity.ISSObject;
import com.example.barbara.skytonight.data.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ISSRemoteDataSource implements ISSDataSource {

    private static ISSRemoteDataSource INSTANCE;
    private RequestQueue queue;
    private final String url = "http://api.open-notify.org/iss-pass.json?n=20";

    private ISSRemoteDataSource() {}

    private ISSRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static ISSRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ISSRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getISSObject(Calendar time, final double latitude, final double longitude, final GetISSObjectCallback callback) {
        final ArrayList<Calendar> flybyTimes = new ArrayList<>();
        final ArrayList<Integer> durations = new ArrayList<>();
        final ISSObject issObject = new ISSObject(flybyTimes, durations, time);
        String url = this.url + "&lat=" + latitude + "&lon=" + longitude;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("response");
                    for (int i = 0; i < 9 && i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        Long risetime = object.getLong("risetime");
                        int duration = object.getInt("duration");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(risetime * 1000);
                        flybyTimes.add(i, calendar);
                        durations.add(i, duration);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onDataLoaded(issObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDataNotAvailable();
            }
        });
        queue.add(jsonObjectRequest);
    }

}
