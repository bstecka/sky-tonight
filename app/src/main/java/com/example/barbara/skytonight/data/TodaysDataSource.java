package com.example.barbara.skytonight.data;
import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;

public interface TodaysDataSource {

    void getAstroObject(Calendar time, int objectId, AstroObjectsDataSource.GetAstroObjectsCallback callback);
}