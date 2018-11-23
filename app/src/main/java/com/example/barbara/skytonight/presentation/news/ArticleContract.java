package com.example.barbara.skytonight.presentation.news;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.util.ArrayList;

public interface ArticleContract {
    interface View extends BaseView<Presenter> {
        void setText(String text);
        String getArticleUrl();
        Context getContext();
        Activity getViewActivity();
        void addArticleViews(ArrayList<android.view.View> views);
    }

    interface Presenter extends BasePresenter {
        void setBaseUrl(String baseUrl);
    }
}
