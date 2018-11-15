package com.example.barbara.skytonight.presentation.core;

import com.example.barbara.skytonight.presentation.BasePresenter;
import com.example.barbara.skytonight.presentation.BaseView;

public class CoreContract {

    interface View extends BaseView<CoreContract.Presenter> {
    }

    interface Presenter extends BasePresenter {
        void refreshLocation();
    }
}
