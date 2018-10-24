package com.example.barbara.skytonight.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.VolleySingleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ArticleFetchService {

    private RequestQueue queue;

    public ArticleFetchService(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public void getNewsArticle(String url, final NewsDataSource.GetNewsArticleCallback callback) {
        final String str = "content";
        Log.e("getNewsArticle", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Element element = document.selectFirst("div .content");
                callback.onDataLoaded(element.wholeText());
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
