package com.example.barbara.skytonight.util;

public class AppConstants {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 99;
    public static double DEFAULT_LATITUDE = -27.104671;
    public static double DEFAULT_LONGITUDE = -109.360481;
    public static String API_URL = "http://skytonight-backend.herokuapp.com/api/v1/";
    public static String ASTRO_OBJECT_API_URL = "https://ssd.jpl.nasa.gov/horizons_batch.cgi?" +
            "batch=1&COMMAND='%d'&MAKE_EPHEM='YES'&TABLE_TYPE='OBSERVER'" +
            "&START_TIME='%s'&STOP_TIME='%s'&STEP_SIZE='30m'&CSV_FORMAT='YES'";
}