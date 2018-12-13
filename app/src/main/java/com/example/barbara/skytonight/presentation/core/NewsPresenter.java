package com.example.barbara.skytonight.presentation.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.barbara.skytonight.AppConstants;
import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.RepositoryFactory;
import com.example.barbara.skytonight.data.repository.NewsRepository;
import com.example.barbara.skytonight.entity.NewsHeadline;

import java.util.ArrayList;
import java.util.List;

public class NewsPresenter implements NewsContract.Presenter {

    private final NewsRepository mNewsRepository;
    private final NewsContract.View mNewsView;
    private final Context context;

    public NewsPresenter(NewsContract.View mNewsView, Context context) {
        this.mNewsRepository = RepositoryFactory.getNewsRepository(context);
        this.mNewsView = mNewsView;
        this.context = context;
    }

    @Override
    public void setUrlsForLanguage() {
        this.mNewsRepository.setUrls(getUrlsForLanguage());
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
                mNewsView.hideErrorText();
                mNewsView.updateList(arrayList);
            }

            @Override
            public void onDataNotAvailable() {
                mNewsView.showErrorText();
                Log.e("NewsPresenter", "DataNotAvailable");
            }
        });
    }

    private String[] getUrlsForLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_EN);
        if (language.equals(AppConstants.LANG_PL))
            return AppConstants.NEWS_PL;
        else
            return AppConstants.NEWS_EN;
    }
}
