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
import com.example.barbara.skytonight.util.AppConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleFetchService {

    private RequestQueue queue;
    private String baseUrl;

    public ArticleFetchService(Context context) {
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        queue = singleton.getRequestQueue();
    }

    public void setBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public void getNewsArticle(String url, final NewsDataSource.GetNewsArticleCallback callback) {
        Log.e("getNewsArticle", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Element element;
                String content = "";
                StringBuilder builder = new StringBuilder();
                if (baseUrl.equals(AppConstants.NEWS_URL_EN)) {
                    element = document.selectFirst("div .content");
                    if (element != null)
                        content = element.wholeText();
                }
                else if (baseUrl.equals(AppConstants.NEWS_URL_PL)) {
                    element = document.selectFirst("div .entry-content");
                    if (element != null) {
                        Elements elements = element.select("p");
                        for (Element el : elements) {
                            if (!el.hasClass("wp-caption-text") && !el.hasClass("news-source") && el.wholeText().trim().length() > 1) {
                                builder.append(el.wholeText());
                                builder.append("\n\n");
                            }
                        }
                        content = builder.toString().trim();
                    }
                }
                else if (baseUrl.equals(AppConstants.NEWS_URL_EN_2)) {
                    element = document.selectFirst("div .article-content");
                    if (element != null) {
                        Elements elements = element.select("p");
                        for (Element el : elements) {
                            if (el.wholeText().trim().length() > 1) {
                                builder.append(el.wholeText().trim());
                                builder.append("\n\n");
                            }
                        }
                        content = builder.toString().trim();
                    }
                }
                callback.onDataLoaded(content);
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
