package com.example.barbara.skytonight.data;

import com.example.barbara.skytonight.entity.AstroObject;

import java.util.Calendar;

public interface AstroObjectsDataSource {

    interface GetAstroObjectsCallback {
        void onDataLoaded(AstroObject object);
        void onDataNotAvailable();
    }

    void getAstroObject(Calendar time, int objectId, GetAstroObjectsCallback callback);
}