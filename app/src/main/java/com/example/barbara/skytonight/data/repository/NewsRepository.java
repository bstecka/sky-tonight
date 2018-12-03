package com.example.barbara.skytonight.data.repository;

import android.content.Context;
import android.util.Log;
import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.entity.NewsHeadline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NewsRepository implements NewsDataSource {

    private static NewsRepository INSTANCE = null;
    private NewsDataSource mNewsDataSource;
    private String[] urls;

    private NewsRepository(NewsDataSource mNewsDataSource) {
        this.mNewsDataSource = mNewsDataSource;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
        mNewsDataSource.setBaseUrl(urls[0]);
    }

    public void setBaseUrl(String url) {
        mNewsDataSource.setBaseUrl(url);
    }

    public static NewsRepository getInstance(NewsDataSource newsDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(newsDataSource);
        }
        return INSTANCE;
    }

    public static NewsRepository getInstance(NewsDataSource newsDataSource, Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(newsDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getNewsHeadlines(final GetNewsHeadlinesCallback callback) {
        final List<NewsHeadline> list = new ArrayList<>();
        for (String url : urls) {
            mNewsDataSource.setBaseUrl(url);
            mNewsDataSource.getNewsHeadlines(new GetNewsHeadlinesCallback() {
                @Override
                public void onDataLoaded(List<NewsHeadline> newsHeadlines) {
                    list.addAll(newsHeadlines);
                    callback.onDataLoaded(list);
                }

                @Override
                public void onDataNotAvailable() {
                    Log.e("NewsRepository", "onDataNotAvailable");
                    callback.onDataNotAvailable();
                }
            });
        }
    }
}
