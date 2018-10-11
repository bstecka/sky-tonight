package com.example.barbara.skytonight.photos;

import com.example.barbara.skytonight.BasePresenter;
import com.example.barbara.skytonight.BaseView;

public class PhotoGalleryContract {

    interface View extends BaseView<Presenter> {
        void refreshListInView();
    }

    interface Presenter extends BasePresenter {
    }
}
