package com.pointproject.pointproject.ui.maps;


import android.content.ContentResolver;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;

import java.util.HashMap;
import java.util.Map;

public interface MapsContract {
    interface Presenter extends BasePresenter<View>{
        void startLocationUpdates();

        void getAreas();

        void setInitialLocation(Location location);

        void getPoints();
    }
    interface View extends BaseView<Presenter>{
        void updateUi(Location location);

        void putAreas(Map<PolygonOptions, String> areas);

        void putPoints(HashMap<String, LatLng> points);

        void showBadInternetConnection();

        void showMockedLocationProhibited();
    }
}