package com.example.barbara.skytonight.core;

import android.content.Context;
import android.location.Location;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.data.AstroObject;

import java.util.ArrayList;

public class TodayContract {

    interface View extends BaseView<Presenter> {
        void updateList(ArrayList<AstroObject> list);
        void clearList();
        void updateList(AstroObject object);
        Context getContext();
    }

    interface Presenter extends BasePresenter {
    }
}