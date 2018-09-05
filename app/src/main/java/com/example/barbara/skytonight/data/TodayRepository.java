package com.example.barbara.skytonight.data;

import java.util.Calendar;
import java.util.List;

public class TodayRepository implements TodaysDataSource {

    private static TodayRepository INSTANCE = null;
    private final AstroObjectsDataSource mAstroObjectsRemoteDataSource;

    public TodayRepository(AstroObjectsDataSource astroObjectsRemoteDataSource) {
        mAstroObjectsRemoteDataSource = astroObjectsRemoteDataSource;
    }

    public static TodayRepository getInstance(AstroObjectsDataSource astroObjectsRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TodayRepository(astroObjectsRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void getAstroObjectFromRemoteRepository(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        mAstroObjectsRemoteDataSource.getAstroObject(time, objectId, callback);
    }

    @Override
    public void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        getAstroObjectFromRemoteRepository(time, objectId, callback);
    }
}
