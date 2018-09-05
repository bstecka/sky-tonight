package com.example.barbara.skytonight.core;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.data.AstroObject;

import java.util.ArrayList;

public class TodayContract {

    interface View extends BaseView<Presenter> {

        void updateList(ArrayList<AstroObject> list);
    }

    interface Presenter extends BasePresenter {

    }
}
