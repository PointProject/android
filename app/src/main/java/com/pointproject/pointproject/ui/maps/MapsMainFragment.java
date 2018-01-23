package com.pointproject.pointproject.ui.maps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Values;
import com.pointproject.pointproject.geofence.GeofenceController;
import com.pointproject.pointproject.ui.maps.areaDetails.AreaDetailsActivity;
import com.pointproject.pointproject.ui.maps.areaDetails.AreaDetailsFragment;
import com.pointproject.pointproject.ui.settings.SettingsActivity;

import java.util.HashMap;
import java.util.Map;


public class MapsMainFragment extends AbstractFragment  implements OnMapReadyCallback ,
        LocationListener,
        GoogleMap.OnPolygonClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int LAYOUT = R.layout.maps_fragment;

    private static final String TAG = MapsMainFragment.class.getSimpleName();

    private static final int PERMISSION_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private static final LatLngBounds ODESSA_LIMITS =  new LatLngBounds(
            new LatLng(46.295105, 30.517616),
            new LatLng(46.652603, 30.917244));

    private GoogleMap mMap;

    private SupportMapFragment mapFragment;

    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    LocationManager locationManager;

    Marker mCurrentLocationMarker;

    private Location currentBestLocation;

    private Map<Polygon, String> polygons;


    //тот самый getInstance
    public static MapsMainFragment getInstance(Context context) {
        MapsMainFragment fragmentInstance = new MapsMainFragment();

        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        fragmentInstance.setContext(context);
        return fragmentInstance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setContext(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);

        //работа с картой через ребенка
        mapFragment = (SupportMapFragment)this.getChildFragmentManager()
                .findFragmentById(R.id.map);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mapFragment.getMapAsync( this);

        isGooglePlayServicesAvailable();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        locationManager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);

        requestTurnOnGPS();
    }

    private void requestTurnOnGPS(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(getResources().getInteger(R.integer.location_request_update_interval));
        locationRequest.setFastestInterval(getResources().getInteger(R.integer.location_request_fastest_interval));
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        //always show dialog window when gps is disabled
        builder.setAlwaysShow(true);


        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult()
                        status.startResolutionForResult(
                                (MapsActivity)getActivity(), 1000);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    break;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        startLocationUpdate();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        locationManager.removeUpdates(this);

        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);

        mMap.setLatLngBoundsForCameraTarget(ODESSA_LIMITS);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ODESSA_LIMITS.getCenter()));


        startLocationUpdate();

        putPolygons();
    }

    private void putGeofences(HashMap<String, LatLng> geofData){
        GeofenceController gc = new GeofenceController(context);
        gc.addGeofences(geofData);

        for(Map.Entry<String, LatLng> entry: geofData.entrySet())
            mMap.addMarker(new MarkerOptions().position(entry.getValue()));
    }

    private void putPolygons(){
        polygons = new HashMap<>();
        mMap.setOnPolygonClickListener(this);
        for(PolygonOptions key : Values.areas.keySet()){
            polygons.put(mMap.addPolygon(key.clickable(true)), Values.areas.get(key));
        }
    }

    private void startLocationUpdate(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, getResources().getInteger(R.integer.network_provider_location_updates), 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, getResources().getInteger(R.integer.gps_provider_location_updates), 0, this);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        String name = polygons.get(polygon);

        Intent intent = AreaDetailsActivity.getIntent(context, name);
        startActivity(intent);
    }

    public void showNoGeoPermissionSnackbar() {
        Snackbar.make(view.findViewById(R.id.map), getText(R.string.no_location_permission), Snackbar.LENGTH_INDEFINITE)
                .setAction(getText(R.string.settings), v -> {
                    openApplicationSettings();

                    Toast.makeText(context,
                            getText(R.string.grant_location_permission),
                            Toast.LENGTH_SHORT)
                            .show();
                })
                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, getString(R.string.permission_granted), Toast.LENGTH_LONG).show();

                    try{
                        startLocationUpdate();
                    } catch (SecurityException e) {
                        Toast.makeText(context, getString(R.string.security_exception) + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    showNoGeoPermissionSnackbar();
                }
                return;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MapsActivity)getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_FINE_LOCATION);

            return;
        }
        Log.d(TAG, "Connected to GoogleApiClient");

        currentBestLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        updateUI(currentBestLocation);
        Log.d(TAG, "LastLocation: " + (currentBestLocation == null ? "NO LastLocation" : currentBestLocation.toString()));
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, getString(R.string.google_api_client_connection_failed) + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "GoogleApiClient On connection failed: " + connectionResult.toString());
    }

    private void updateUI(Location loc) {
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();

        Log.d(TAG, "updateUI");
        Log.d(TAG, "Latitude: " + lat + " Longitude: " + lng +
                "\nAccuracy: " + loc.getAccuracy() +
                "\nProvider: " + loc.getProvider());


        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lng));
        if(mCurrentLocationMarker == null){
            mCurrentLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(loc.getLatitude(), loc.getLongitude()),
                            15));
            return;
        }

        changeMarkerPositionSmoothly(mCurrentLocationMarker, new LatLng(lat, lng));
    }

    private void moveCameraSmoothly(Location location){
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),location.getLongitude()))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 10000, null);
    }

    public void changeMarkerPositionSmoothly(final Marker marker, final LatLng toPosition) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        Log.d(TAG, "Marker position updated");

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private boolean isGooglePlayServicesAvailable() {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "Google Play Services. This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        Log.d(TAG, "Google Play Services. This device is supported.");
        return true;
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

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if(isBetterLocation(location, currentBestLocation)){
                currentBestLocation = location;
                updateUI(location);
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
}
