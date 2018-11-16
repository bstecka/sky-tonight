package com.example.barbara.skytonight.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.AppConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CoreRepository implements CoreDataSource {

    private static CoreRepository INSTANCE;
    private boolean requestedPermission = false;

    private CoreRepository(){}

    public static CoreRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CoreRepository();
        }
        return INSTANCE;
    }

    @Override
    public void getUserLocation(final Context context, final CoreDataSource.GetUserLocationCallback callback) {
        //Log.e("CoreRepository", "getUserLocation, boolean: " + requestedPermission);
        boolean noFineLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean noCoarseLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (noFineLocationPermission && noCoarseLocationPermission) {
            callback.onDataNotAvailable();
        } else {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.e("CoreRepository", "mFusedLocationClient success " + location.getLatitude() + " " + location.getLongitude());
                        callback.onDataLoaded(location);
                    } else {
                        Log.e("CoreRepository", "mFusedLocationClient returned null");
                        callback.onDataNotAvailable();
                        Toast.makeText(context, R.string.core_no_location_toast, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void getUserLocation(final Activity activity, final CoreDataSource.GetUserLocationCallback callback) {
        //Log.e("CoreRepository", "getUserLocation, boolean: " + requestedPermission);
        boolean noFineLocationPermission = ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean noCoarseLocationPermission = ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (noFineLocationPermission && noCoarseLocationPermission) {
            if (!requestedPermission) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                requestedPermission = true;
                callback.onRequestForPermission();
            }
            else {
                callback.onDataNotAvailable();
            }
        } else {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity.getApplicationContext());
            mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.e("CoreRepository", "mFusedLocationClient success " + location.getLatitude() + " " + location.getLongitude());
                        callback.onDataLoaded(location);
                    } else {
                        Log.e("CoreRepository", "mFusedLocationClient returned null");
                        callback.onDataNotAvailable();
                        Toast.makeText(activity.getApplicationContext(), R.string.core_no_location_toast, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
