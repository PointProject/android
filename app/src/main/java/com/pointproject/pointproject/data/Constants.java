package com.pointproject.pointproject.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Constants {

    public static final String LOG_TAG = "LOG";

    //server info
    public static final String SERVER_HOST = "";


    public static final LatLngBounds ODESSA_LIMITS =  new LatLngBounds(
            new LatLng(46.295105, 30.517616),
            new LatLng(46.652603, 30.917244));

}
