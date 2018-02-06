package com.pointproject.pointproject.ui.maps;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.ArraySet;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.pointproject.pointproject.PointProjectApplication;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Values;
import com.pointproject.pointproject.model.Point;
import com.pointproject.pointproject.model.Zone;
import com.pointproject.pointproject.network.ApiClient;
import com.pointproject.pointproject.network.callback.GetZoneCallback;
import com.pointproject.pointproject.network.response.NetworkError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import static com.pointproject.pointproject.data.Constants.KEY_TOKEN;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;

public class MapsPresenter implements MapsContract.Presenter,
        LocationListener {

    @Inject LocationManager locationManager;

    @Inject ApiClient apiClient;

    private MapsContract.View mapsView;

    private Context mContext;

    private Location currentBestLocation;

    @Inject
    MapsPresenter(MapsActivity mapsActivity){
        mContext = mapsActivity.getBaseContext();
    }

    @Override
    public void takeView(MapsContract.View view) {
        mapsView = view;
        startLocationUpdates();
    }

    @Override
    public void dropView() {
        locationManager.removeUpdates(this);
        mapsView = null;
    }

    @Override
    public void setInitialLocation(Location location){
        currentBestLocation = location;
    }

    @Override
    public void getPoints() {

    }

    @Override
    public void startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                mContext.getResources().getInteger(R.integer.network_provider_location_updates),
                0,
                this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                mContext.getResources().getInteger(R.integer.gps_provider_location_updates),
                0,
                this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if(isBetterLocation(location, currentBestLocation)){
                currentBestLocation = location;
                mapsView.updateUi(location);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void getAreas() {
        Map<PolygonOptions, String> areas = new HashMap<>();

        apiClient.getZone(new GetZoneCallback() {
            @Override
            public void onSuccess(List<Zone> zones) {
                for(Zone zone : zones){
                    PolygonOptions po = new PolygonOptions();
                    List<LatLng> latLngs = new ArrayList<>();
                    if(zone.getPoints().isEmpty())
                        continue;
                    for(Point point: zone.getPoints()){
                        double lat = point.getLatitude();
                        double lon = point.getLongitude();
                        latLngs.add(new LatLng(lat, lon));
                    }
                    po.addAll(latLngs);
                    po.fillColor(Color.parseColor(zone.getFillColor()));
                    po.strokeColor(Color.parseColor(zone.getStrokeColor()));

                    areas.put(po, zone.getTitle());
                }

                mapsView.putAreas(areas);
            }

            @Override
            public void onError(NetworkError error) {
                mapsView.showBadInternetConnection();
            }
        });
    }

    /**Determines whether one Location reading is better than the current Location fix*/
    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }
        final int THIRTY_SECONDS = 1000 * 30;

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > THIRTY_SECONDS;
        boolean isSignificantlyOlder = timeDelta < -THIRTY_SECONDS;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 20;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
