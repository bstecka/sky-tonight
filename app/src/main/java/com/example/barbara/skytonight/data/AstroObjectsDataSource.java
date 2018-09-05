package com.example.barbara.skytonight.data;

import java.util.Calendar;
import java.util.List;

public interface AstroObjectsDataSource {

    interface GetAstroObjectsCallback {

        void onDataLoaded(String response, int objectId);

        void onDataNotAvailable();
    }

    List<AstroObject> getAstroObjects(Calendar time, GetAstroObjectsCallback callback);
}