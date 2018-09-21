package com.example.barbara.skytonight.core;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

public class CoreContract {

    interface View extends BaseView<CoreContract.Presenter> {
    }

    interface Presenter extends BasePresenter {
        void refreshLocation();
    }
}
