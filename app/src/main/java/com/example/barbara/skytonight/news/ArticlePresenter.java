package com.example.barbara.skytonight.news;
import android.util.Log;

import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.NewsRepository;

public class ArticlePresenter implements ArticleContract.Presenter {

    private final NewsRepository newsRepository;
    private final ArticleContract.View articleView;

    public ArticlePresenter(NewsRepository newsRepository, ArticleContract.View articleView) {
        this.newsRepository = newsRepository;
        this.articleView = articleView;
    }

    @Override
    public void start() {
        if (articleView.getArticleUrl() != null)
            getNewsArticle(articleView.getArticleUrl());
    }

    private void getNewsArticle(String url) {
        newsRepository.getNewsArticle(url, new NewsDataSource.GetNewsArticleCallback() {
            @Override
            public void onDataLoaded(String content) {
                articleView.setText(content);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("ArticlePresenter", "data not available");
            }
        });
    }
}
