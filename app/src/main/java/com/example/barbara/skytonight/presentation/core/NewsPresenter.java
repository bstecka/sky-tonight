package com.example.barbara.skytonight.presentation.core;
import android.content.Context;
import android.util.Log;

import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.repository.NewsRepository;
import com.example.barbara.skytonight.entity.NewsHeadline;

import java.util.ArrayList;
import java.util.List;

public class NewsPresenter implements NewsContract.Presenter {

    private final NewsRepository mNewsRepository;
    private final NewsContract.View mNewsView;

    public NewsPresenter(NewsRepository mNewsRepository, NewsContract.View mNewsView) {
        this.mNewsRepository = mNewsRepository;
        this.mNewsView = mNewsView;
    }

    @Override
    public void start() {
        getNews();
    }

    private void getNews() {
        mNewsView.clearList();
        mNewsRepository.getNewsHeadlines(new NewsDataSource.GetNewsHeadlinesCallback() {
            @Override
            public void onDataLoaded(List<NewsHeadline> newsHeadlines) {
                ArrayList<NewsHeadline> arrayList = (ArrayList<NewsHeadline>) newsHeadlines;
                mNewsView.updateList(arrayList);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("NewsPresenter", "DataNotAvailable");
            }
        });
    }
}
