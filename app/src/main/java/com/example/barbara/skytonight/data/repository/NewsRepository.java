package com.example.barbara.skytonight.data.repository;

import android.util.Log;

import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.entity.NewsHeadline;
import com.example.barbara.skytonight.data.remote.ArticleFetchService;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository implements NewsDataSource {

    private static NewsRepository INSTANCE = null;
    private NewsDataSource mNewsDataSource;
    private ArticleFetchService mArticleFetchService;

    private NewsRepository(NewsDataSource mNewsDataSource, ArticleFetchService mArticleFetchService) {
        this.mNewsDataSource = mNewsDataSource;
        this.mArticleFetchService = mArticleFetchService;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        mArticleFetchService.setBaseUrl(baseUrl);
        mNewsDataSource.setBaseUrl(baseUrl);
    }

    public static NewsRepository getInstance(NewsDataSource newsDataSource, ArticleFetchService articleFetchService) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(newsDataSource, articleFetchService);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getNewsHeadlines(final GetNewsHeadlinesCallback callback) {
        final List<NewsHeadline> list = new ArrayList<>();
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

    public void getNewsArticle(String url, GetNewsArticleCallback callback) {
        mArticleFetchService.getNewsArticle(url, callback);
    }
}
