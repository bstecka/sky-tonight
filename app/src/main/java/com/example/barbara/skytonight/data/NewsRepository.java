package com.example.barbara.skytonight.data;

import android.util.Log;

import com.example.barbara.skytonight.core.NewsHeadline;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository implements NewsDataSource {

    private static NewsRepository INSTANCE = null;
    private NewsDataSource newsDataSource;

    private NewsRepository(NewsDataSource newsDataSource) {
        this.newsDataSource = newsDataSource;
    }

    public static NewsRepository getInstance(NewsDataSource newsDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(newsDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private void getNewsHeadlinesFromRemoteRepository(final GetNewsHeadlinesCallback callback){
        final List<NewsHeadline> list = new ArrayList<>();
        newsDataSource.getNewsHeadlines(new GetNewsHeadlinesCallback() {
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

    @Override
    public void getNewsHeadlines(GetNewsHeadlinesCallback callback) {
        getNewsHeadlinesFromRemoteRepository(callback);
    }
}
