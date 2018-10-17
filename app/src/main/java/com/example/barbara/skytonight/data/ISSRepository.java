package com.example.barbara.skytonight.data;

public class ISSRepository implements ISSDataSource {

    private static ISSRepository INSTANCE = null;
    private final ISSDataSource mISSDataSource;

    private ISSRepository(ISSDataSource mISSDataSource) {
        this.mISSDataSource = mISSDataSource;
    }

    public static ISSRepository getInstance(ISSDataSource mISSDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ISSRepository(mISSDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private void getISSObjectFromRemoteRepository(double latitude, double longitude, GetISSObject callback) {
        mISSDataSource.getISSObject(latitude, longitude, callback);
    }

    @Override
    public void getISSObject(double latitude, double longitude, GetISSObject callback) {
        getISSObjectFromRemoteRepository(latitude, longitude, callback);
    }
}
