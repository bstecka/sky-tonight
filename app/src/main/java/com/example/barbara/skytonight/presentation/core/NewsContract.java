package com.example.barbara.skytonight.presentation.core;

import com.example.barbara.skytonight.entity.NewsHeadline;
import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

import java.util.ArrayList;

public class NewsContract {

    interface View extends BaseView<Presenter> {
        void clearList();
        void updateList(ArrayList<NewsHeadline> list);
        void showErrorText();
        void hideErrorText();
    }

    interface Presenter extends BasePresenter {
        void setUrlsForLanguage();
    }
}
