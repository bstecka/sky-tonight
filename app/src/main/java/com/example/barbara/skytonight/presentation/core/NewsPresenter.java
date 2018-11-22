package com.example.barbara.skytonight.presentation.core;

import android.util.Log;

import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.repository.NewsRepository;
import com.example.barbara.skytonight.entity.NewsHeadline;

import java.util.ArrayList;
import java.util.List;

public class NewsPresenter implements NewsContract.Presenter {

    private final NewsRepository newsRepository;
    private final NewsContract.View newsView;

    public NewsPresenter(NewsRepository newsRepository, NewsContract.View newsView) {
        this.newsRepository = newsRepository;
        this.newsView = newsView;
    }

    @Override
    public void start() {
        getNews();
    }

    private void getNews() {
        newsView.clearList();
        newsRepository.getNewsHeadlines(new NewsDataSource.GetNewsHeadlinesCallback() {
            @Override
            public void onDataLoaded(List<NewsHeadline> newsHeadlines) {
                ArrayList<NewsHeadline> arrayList = (ArrayList<NewsHeadline>) newsHeadlines;
                newsView.updateList(arrayList);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("NewsPresenter", "DataNotAvailable");
            }
        });
    }
}
