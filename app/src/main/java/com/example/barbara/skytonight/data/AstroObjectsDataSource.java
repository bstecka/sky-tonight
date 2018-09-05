package com.example.barbara.skytonight.data;

import java.util.Calendar;
import java.util.List;

public interface AstroObjectsDataSource {

    interface GetAstroObjectsCallback {

        void onDataLoaded(String response, int objectId);

        void onDataNotAvailable();
    }

    void getAstroObject(Calendar time, int objectId, GetAstroObjectsCallback callback);
}