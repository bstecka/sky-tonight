package com.example.barbara.skytonight;

public class AppConstants {

    public static final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME = "Verbose WorkManager Notifications";
    public static String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts";
    public static final String CHANNEL_ID = "VERBOSE_NOTIFICATION" ;
    public static final int NOTIFICATION_ID = 1;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 98;
    public static double DEFAULT_LATITUDE = -27.104671;
    public static double DEFAULT_LONGITUDE = -109.360481;
    public static String API_URL = "http://skytonight-backend.herokuapp.com/api/v1/";
    public static String ASTRO_OBJECT_API_URL = "https://ssd.jpl.nasa.gov/horizons_batch.cgi?" +
            "batch=1&COMMAND='%d'&MAKE_EPHEM='YES'&TABLE_TYPE='OBSERVER'" +
            "&START_TIME='%s'&STOP_TIME='%s'&STEP_SIZE='30m'&CSV_FORMAT='YES'";
    public static String NEWS_URL_PL = "https://news.astronet.pl/index.php/feed/";
    public static String NEWS_URL_EN = "https://www.space.com/home/feed/site.xml";
    public static String PREF_KEY_LANG = "language";
    public static String PREF_KEY_NOTIF_ISS = "notif-iss";
    public static String PREF_KEY_NOTIF_EVENTS = "notif-events";
    public static String LANG_PL = "pl";
    public static String LANG_EN = "en";

}
