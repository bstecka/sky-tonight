package com.example.barbara.skytonight.util;

public class AppConstants {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 98;
    public static double DEFAULT_LATITUDE = -27.104671;
    public static double DEFAULT_LONGITUDE = -109.360481;
    public static String API_URL = "http://skytonight-backend.herokuapp.com/api/v1/";
    public static String ASTRO_OBJECT_API_URL = "https://ssd.jpl.nasa.gov/horizons_batch.cgi?" +
            "batch=1&COMMAND='%d'&MAKE_EPHEM='YES'&TABLE_TYPE='OBSERVER'" +
            "&START_TIME='%s'&STOP_TIME='%s'&STEP_SIZE='30m'&CSV_FORMAT='YES'";
    public static int TAP_TYPE_DAY = 1;
    public static int TAB_TYPE_WEEK = 2;
    public static int TAB_TYPE_MONTH = 3;
    public static String NEWS_URL_PL = "https://news.astronet.pl/index.php/feed/";
    public static String NEWS_URL_EN = "http://www.astronomy.com/rss/news";
    public static String NEWS_URL_EN_2 = "https://www.space.com/home/feed/site.xml";
    public static String NEWS_URL = NEWS_URL_EN_2;
}
