package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.core.NewsHeadline;

import java.util.List;

public interface NewsDataSource {

    interface GetNewsHeadlinesCallback {
        void onDataLoaded(List<NewsHeadline> newsHeadlines);
        void onDataNotAvailable();
    }

    interface GetNewsArticleCallback {
        void onDataLoaded(String content);
        void onDataNotAvailable();
    }

    void getNewsHeadlines(GetNewsHeadlinesCallback callback);
}
