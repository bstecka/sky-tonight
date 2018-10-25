package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.barbara.skytonight.core.NewsHeadline;
import com.example.barbara.skytonight.data.MeteorShowerEvent;
import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.VolleySingleton;
import com.example.barbara.skytonight.util.AppConstants;
import com.google.android.gms.common.util.IOUtils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class NewsRemoteDataSource implements NewsDataSource {

    private static NewsRemoteDataSource INSTANCE;
    private RequestQueue queue;
    private String url;

    private NewsRemoteDataSource() {}

    @Override
    public void setBaseUrl(String baseUrl) {
        this.url = baseUrl;
    }

    private NewsRemoteDataSource(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public static NewsRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getNewsHeadlines(final GetNewsHeadlinesCallback callback) {
        final List<NewsHeadline> newsHeadlines = new ArrayList<>();
        Log.e("getNewsHeadlines", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SAXReader reader = new SAXReader();
                InputStream in = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                try {
                    Document document = reader.read(in);
                    List<Node> list = document.selectNodes("//channel/item");
                    int count = 0;
                    for (Iterator<Node> iter = list.iterator(); iter.hasNext() && count < 20; count++) {
                        Element element = (Element) iter.next();
                        String title = element.selectSingleNode(".//title").getText();
                        String link = element.selectSingleNode(".//link").getText();
                        String pubDate = element.selectSingleNode(".//pubDate").getText();
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                        Calendar calendar = Calendar.getInstance();
                        try {
                            calendar.setTime(sdf.parse(pubDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.e("NewsError", e.toString());
                        }
                        NewsHeadline newsHeadline = new NewsHeadline(title, link, calendar);
                        newsHeadlines.add(newsHeadline);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                callback.onDataLoaded(newsHeadlines);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NewsRemoteDataSource", error.toString());
                callback.onDataNotAvailable();
            }
        });
        queue.add(request);
    }
}
