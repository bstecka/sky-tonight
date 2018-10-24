package com.example.barbara.skytonight.news;

import android.app.Activity;
import android.content.Context;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

public interface ArticleContract {
    interface View extends BaseView<Presenter> {
        void setText(String text);
        String getArticleUrl();
        Context getContext();
        Activity getViewActivity();
    }

    interface Presenter extends BasePresenter {
    }
}
