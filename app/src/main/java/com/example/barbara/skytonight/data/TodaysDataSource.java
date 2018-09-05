package com.example.barbara.skytonight.data;
import java.util.Calendar;
import java.util.List;

public interface TodaysDataSource {

    List<AstroObject> getAstroObjects(Calendar time, AstroObjectsDataSource.GetAstroObjectsCallback callback);
}