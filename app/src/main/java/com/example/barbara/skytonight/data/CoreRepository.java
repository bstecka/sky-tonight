package com.example.barbara.skytonight.data;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.util.AppConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CoreRepository implements CoreDataSource {

    private boolean requestedPermission = false;

    @Override
    public void getUserLocation(final Activity activity, final GetUserLocationCallback callback) {
        boolean noFineLocationPermission = ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean noCoarseLocationPermission = ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (noFineLocationPermission && noCoarseLocationPermission && !requestedPermission) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            requestedPermission = true;
            callback.onDataNotAvailable();
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity.getApplicationContext());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    callback.onDataLoaded(location);
                else {
                    callback.onDataNotAvailable();
                    Toast.makeText(activity.getApplicationContext(), R.string.core_no_location_toast, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
