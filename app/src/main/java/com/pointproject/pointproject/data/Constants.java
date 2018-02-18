package com.pointproject.pointproject.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Constants {

    public static final String LOG_TAG = "LOG";

//    Shared Preferences
    public static final String NAME_SHARED_PREFERENCES = "com.point.project";
    public static final String KEY_TOKEN = "com.point.project.token";
    public static final String KEY_USER = "com.point.project.user";

    //server info
    public static final String SERVER_HOST = "http://18.220.89.28:8080";


    public static final LatLngBounds ODESSA_LIMITS =  new LatLngBounds(
            new LatLng(46.295105, 30.517616),
            new LatLng(46.652603, 30.917244));

}
