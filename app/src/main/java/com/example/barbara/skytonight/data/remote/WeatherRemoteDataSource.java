package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.skytonight.data.VolleySingleton;
import com.example.barbara.skytonight.data.WeatherDataSource;
import com.example.barbara.skytonight.entity.WeatherObject;
import com.example.barbara.skytonight.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeatherRemoteDataSource implements WeatherDataSource {

    private static WeatherRemoteDataSource INSTANCE;
    private RequestQueue queue;
    private final String url = AppConstants.API_URL + "weather";

    private WeatherRemoteDataSource() {}

    private WeatherRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static WeatherRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherRemoteDataSource(context);
        }
        return INSTANCE;
    }

    public void getWeatherObjects(final double latitude, final double longitude, final GetWeatherObjectsCallback callback) {
        final List<WeatherObject> weatherObjects = new ArrayList<>();
        String url = this.url + "?lat=" + latitude + "&lng=" + longitude;
        Log.e("getWeatherObjects", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("list");
                    for (int i = 0; i < 9 && i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        JSONObject clouds = object.getJSONObject("clouds");
                        JSONObject weather = object.getJSONArray("weather").getJSONObject(0);
                        int coverage = clouds.getInt("all");
                        int id = weather.getInt("id");
                        Long time = object.getLong("dt");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(time * 1000);
                        WeatherObject weatherObject = new WeatherObject(id, coverage, calendar);
                        weatherObjects.add(weatherObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onDataLoaded(weatherObjects);
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
