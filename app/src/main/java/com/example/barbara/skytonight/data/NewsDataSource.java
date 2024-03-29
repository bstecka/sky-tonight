package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.entity.NewsHeadline;

import java.util.List;

public interface NewsDataSource {

    interface GetNewsHeadlinesCallback {
        void onDataLoaded(List<NewsHeadline> newsHeadlines);
        void onDataNotAvailable();
    }

    void getNewsHeadlines(GetNewsHeadlinesCallback callback);
    void setBaseUrl(String baseUrl);
}
