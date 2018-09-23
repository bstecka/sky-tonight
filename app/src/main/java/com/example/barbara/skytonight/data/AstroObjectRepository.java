package com.example.barbara.skytonight.data;

import java.util.Calendar;

public class AstroObjectRepository implements TodaysDataSource {

    private static AstroObjectRepository INSTANCE = null;
    private final AstroObjectsDataSource mAstroObjectsRemoteDataSource;

    public AstroObjectRepository(AstroObjectsDataSource astroObjectsRemoteDataSource) {
        mAstroObjectsRemoteDataSource = astroObjectsRemoteDataSource;
    }

    public static AstroObjectRepository getInstance(AstroObjectsDataSource astroObjectsRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AstroObjectRepository(astroObjectsRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private void getAstroObjectFromRemoteRepository(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        mAstroObjectsRemoteDataSource.getAstroObject(time, objectId, callback);
    }

    @Override
    public void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        getAstroObjectFromRemoteRepository(time, objectId, callback);
    }
}
