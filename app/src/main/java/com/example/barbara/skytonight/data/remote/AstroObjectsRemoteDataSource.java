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
    public List<AstroObject> getAstroObjects(Calendar time, GetAstroObjectsCallback callback) {
        return null;
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

    private AstroObject downloadContent(URL url, int objectId) throws IOException {
        HttpURLConnection con = null;
        AstroObject astroObject = new AstroObject();
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            int status = con.getResponseCode();
            System.out.println("Status: " + status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            in.readLine();
            String objectName = Arrays.asList(in.readLine().split(" ")).get(21);
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

    private AstroObject getAstroObject(final int objectId, Calendar time, final GetAstroObjectsCallback callback)
    {
        String RA, decl;
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UT1"));
        String str_date = isoFormat.format(time.getTime());
        System.out.println("Object " + objectId + ", " + str_date.replaceAll("%20", " "));
        time.add(Calendar.HOUR, 1);
        String str_date_end = isoFormat.format(time.getTime());
        String url = "https://ssd.jpl.nasa.gov/horizons_batch.cgi?batch=1&COMMAND='" + objectId + "'&MAKE_EPHEM='YES'&TABLE_TYPE='OBSERVER'&START_TIME='" + str_date + "'&STOP_TIME='" + str_date_end + "'&STEP_SIZE='30m'&CSV_FORMAT='YES'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onDataLoaded(response, objectId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Request", "Error");
            }
        });
        return new AstroObject();
    }
}
