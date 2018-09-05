package com.example.barbara.skytonight.data;
import java.util.Calendar;
import java.util.List;

public interface TodaysDataSource {

    void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback);
}