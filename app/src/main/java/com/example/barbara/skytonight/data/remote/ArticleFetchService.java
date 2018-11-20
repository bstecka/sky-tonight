package com.example.barbara.skytonight.data.remote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.VolleySingleton;
import com.example.barbara.skytonight.entity.ArticleContentWrapper;
import com.example.barbara.skytonight.AppConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

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
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Element element;
                StringBuilder builder = new StringBuilder();
                ArrayList<ArticleContentWrapper> articleChunks = new ArrayList<>();
                if (baseUrl.equals(AppConstants.NEWS_URL_PL)) {
                    element = document.selectFirst("div .entry-content");
                    if (element != null) {
                        Elements elements = element.getAllElements();
                        for (Element el : elements) {
                            if (el.is("p")&& !el.hasClass("wp-caption-text") && !el.hasClass("news-source") && el.wholeText().trim().length() > 1) {
                                builder.append(el.wholeText());
                                ArticleContentWrapper chunk = new ArticleContentWrapper(el.wholeText().trim(), false);
                                articleChunks.add(chunk);
                                builder.append("\n\n");
                            }
                            if (el.is("div") && el.hasClass("media-credit-container") && el.select("img").size() > 0) {
                                Element img = el.selectFirst("img");
                                ArticleContentWrapper chunk = new ArticleContentWrapper(img.attr("abs:src"),true);
                                articleChunks.add(chunk);
                            }
                        }
                    }
                }
                else if (baseUrl.equals(AppConstants.NEWS_URL_EN)) {
                    Element image = document.selectFirst("img");
                    element = document.selectFirst("div .article-content");
                    if (element != null) {
                        Elements elements = element.select("p");
                        for (Element el : elements) {
                            if (el.wholeText().trim().length() > 1) {
                                builder.append(el.wholeText().trim());
                                ArticleContentWrapper chunk = new ArticleContentWrapper(el.wholeText().trim(), false);
                                articleChunks.add(chunk);
                                builder.append("\n\n");
                            }
                        }
                    }
                }
                callback.onDataLoaded(articleChunks);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onDataNotAvailable();
            }
        });
        queue.add(request);
    }
}
