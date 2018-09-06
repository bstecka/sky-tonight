package com.example.barbara.skytonight.core;

import android.location.Location;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;
import com.example.barbara.skytonight.data.AstroObject;

import java.util.ArrayList;

public class CoreContract {

    interface View extends BaseView<CoreContract.Presenter> {
    }

    interface Presenter extends BasePresenter {
        void refreshLocation();
    }
}
