package com.example.barbara.skytonight.news;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

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
    }
}
