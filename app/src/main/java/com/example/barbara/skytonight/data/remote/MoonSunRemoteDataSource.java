package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.barbara.skytonight.data.ISSDataSource;
import com.example.barbara.skytonight.data.MoonSunDataSource;
import com.example.barbara.skytonight.data.VolleySingleton;
import com.example.barbara.skytonight.entity.ISSObject;
import com.example.barbara.skytonight.entity.MoonSunData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MoonSunRemoteDataSource implements MoonSunDataSource {

    private static MoonSunRemoteDataSource INSTANCE;
    private RequestQueue queue;
    private final String url = "http://api.usno.navy.mil/rstt/oneday?";

    private MoonSunRemoteDataSource() {}

    private MoonSunRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static MoonSunRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MoonSunRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getMoonSunData(final Calendar time, final double latitude, final double longitude, final GetMoonSunDataCallback callback) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.getDefault());
        String url = this.url + "date=" + sdf.format(time.getTime()) + "&coords=" + latitude + "," + longitude +"&tz=0";
        Log.e("getMoonSunData", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray sundata = response.getJSONArray("sundata");
                    String sunrise = sundata.getJSONObject(1).getString("time");
                    String sunset = sundata.getJSONObject(3).getString("time");
                    JSONArray moondata = response.getJSONArray("moondata");
                    String moonrise = moondata.getJSONObject(0).getString("time");
                    String moonset = moondata.getJSONObject(2).getString("time");
                    Calendar sunriseTime = getTime(time, sunrise);
                    Calendar sunsetTime = getTime(time, sunset);
                    Calendar moonriseTime = getTime(time, moonrise);
                    Calendar moonsetTime = getTime(time, moonset);
                    if (sunsetTime.getTimeInMillis() < sunriseTime.getTimeInMillis())
                        sunsetTime.add(Calendar.DAY_OF_YEAR, 1);
                    if (moonsetTime.getTimeInMillis() < moonriseTime.getTimeInMillis())
                        moonsetTime.add(Calendar.DAY_OF_YEAR, 1);
                    MoonSunData moonSunData = new MoonSunData(moonriseTime, moonsetTime, sunriseTime, sunsetTime);
                    callback.onDataLoaded(moonSunData);
                } catch (JSONException e) {
                    callback.onDataNotAvailable();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDataNotAvailable();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private Calendar getTime(Calendar baseTime, String hourString) {
        Calendar object = Calendar.getInstance();
        object.setTime(baseTime.getTime());
        int hour = Integer.parseInt(hourString.substring(0,2));
        int minute = Integer.parseInt(hourString.substring(3));
        hour += (object.getTimeZone().getRawOffset()/3600000)%24;
        if (hour < 0){
            hour = 24 - hour;
        }
        object.set(Calendar.HOUR_OF_DAY, hour);
        object.set(Calendar.MINUTE, minute);
        return object;
    }
}
