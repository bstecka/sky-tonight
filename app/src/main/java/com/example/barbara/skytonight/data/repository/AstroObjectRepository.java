package com.example.barbara.skytonight.data.repository;

import com.example.barbara.skytonight.data.AstroObjectsDataSource;

import java.util.Calendar;

public class AstroObjectRepository implements AstroObjectsDataSource {

    private static AstroObjectRepository INSTANCE = null;
    private final AstroObjectsDataSource mAstroObjectsDataSource;

    private AstroObjectRepository(AstroObjectsDataSource astroObjectsDataSource) {
        mAstroObjectsDataSource = astroObjectsDataSource;
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

    @Override
    public void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback) {
        mAstroObjectsDataSource.getAstroObject(time, objectId, callback);
    }
}
