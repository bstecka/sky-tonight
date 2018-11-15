package com.example.barbara.skytonight.data;

import android.util.Log;

import com.example.barbara.skytonight.entity.NewsHeadline;
import com.example.barbara.skytonight.data.remote.ArticleFetchService;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository implements NewsDataSource {

    private static NewsRepository INSTANCE = null;
    private NewsDataSource newsDataSource;
    private ArticleFetchService articleFetchService;

    private NewsRepository(NewsDataSource newsDataSource, ArticleFetchService articleFetchService) {
        this.newsDataSource = newsDataSource;
        this.articleFetchService = articleFetchService;
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        articleFetchService.setBaseUrl(baseUrl);
        newsDataSource.setBaseUrl(baseUrl);
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

    private void getNewsArticleFromRemoteRepository(String url, final GetNewsArticleCallback callback){
        articleFetchService.getNewsArticle(url, callback);
    }

    @Override
    public void getNewsHeadlines(GetNewsHeadlinesCallback callback) {
        getNewsHeadlinesFromRemoteRepository(callback);
    }

    public void getNewsArticle(String url, GetNewsArticleCallback callback) {
        getNewsArticleFromRemoteRepository(url, callback);
    }
}
