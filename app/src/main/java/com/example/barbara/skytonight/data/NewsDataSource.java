package com.example.barbara.skytonight.data;

import android.graphics.Bitmap;

import com.example.barbara.skytonight.core.NewsHeadline;
import com.example.barbara.skytonight.photos.ImageFile;

import java.util.ArrayList;
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
    void setBaseUrl(String baseUrl);
}
