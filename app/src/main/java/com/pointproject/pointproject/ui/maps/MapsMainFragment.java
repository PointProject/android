package com.pointproject.pointproject.ui.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.geofence.GeofenceController;
import com.pointproject.pointproject.ui.areaDetails.AreaDetailsActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.pointproject.pointproject.data.Constants.ODESSA_LIMITS;


public class MapsMainFragment extends AbstractFragment  implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MapsContract.View {

    private static final int LAYOUT = R.layout.maps_fragment;

    private static final String TAG = MapsMainFragment.class.getSimpleName();

    private static final int PERMISSION_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private static final String KEY_LOCATION_DIALOG_PERMISSION = "com.pointproject.location";

    @Inject
    LocationSettingsRequest.Builder locationSettingsBuilder;

    @Inject
    MapsContract.Presenter presenter;

    private GoogleApiClient mGoogleApiClient;

    private GoogleMap mMap;

    private SupportMapFragment mapFragment;

    private Marker mCurrentLocationMarker;

    private Map<Polygon, String> polygons;

    /** Prevents permission dialog from duplication on screen rotation*/
    private boolean alreadyAskedForLocationPermission = false;

    @Inject
    public MapsMainFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        presenter.takeView(this);
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        presenter.dropView();
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setContext(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        //работа с картой через ребенка
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        if(savedInstanceState!=null)
            alreadyAskedForLocationPermission = savedInstanceState.getBoolean(
                    KEY_LOCATION_DIALOG_PERMISSION, false);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_LOCATION_DIALOG_PERMISSION, alreadyAskedForLocationPermission);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment.getMapAsync(this);

        isGooglePlayServicesAvailable();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        requestTurnOnGPS();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);

        mMap.setLatLngBoundsForCameraTarget(ODESSA_LIMITS);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ODESSA_LIMITS.getCenter()));

        presenter.getAreas();
    }


    private void requestTurnOnGPS() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        locationSettingsBuilder.build());
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
                                getActivity(), 1000);
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
    public void putPoints(HashMap<String, LatLng> geofData) {
        GeofenceController gc = new GeofenceController(context);
        gc.addGeofences(geofData);

        for (Map.Entry<String, LatLng> entry : geofData.entrySet())
            mMap.addMarker(new MarkerOptions().position(entry.getValue()));
    }

    @Override
    public void putAreas(Map<PolygonOptions, String> areas) {
        mMap.setOnPolygonClickListener(this);
        polygons = new HashMap<>();
        for (PolygonOptions key : areas.keySet()) {
            polygons.put(mMap.addPolygon(key.clickable(true)), areas.get(key));
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        String name = polygons.get(polygon);

        Intent intent = AreaDetailsActivity.getIntent(context, name);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(alreadyAskedForLocationPermission)
            return;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            alreadyAskedForLocationPermission = true;
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_FINE_LOCATION);

            return;
        }
        Log.d(TAG, "Connected to GoogleApiClient");

        Location initialLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        presenter.setInitialLocation(initialLocation);
        updateUi(initialLocation);
        Log.d(TAG, "LastLocation: " + (initialLocation == null ? "NO LastLocation" : initialLocation.toString()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, getString(R.string.permission_granted), Toast.LENGTH_LONG).show();

                    try {
                        alreadyAskedForLocationPermission = false;
                        presenter.startLocationUpdates();
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
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, getString(R.string.google_api_client_connection_failed) + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "GoogleApiClient On connection failed: " + connectionResult.toString());
    }

    @Override
    public void updateUi(Location loc) {
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();

        Log.d(TAG, "updateUI");
        Log.d(TAG, "Latitude: " + lat + " Longitude: " + lng +
                "\nAccuracy: " + loc.getAccuracy() +
                "\nProvider: " + loc.getProvider());


        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lng));
        if (mCurrentLocationMarker == null) {
            mCurrentLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(loc.getLatitude(), loc.getLongitude()),
                            15));
            return;
        }

        changeMarkerPositionSmoothly(mCurrentLocationMarker, new LatLng(lat, lng));
    }

    @Override
    public void showBadInternetConnection() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.bad_internet_connection)
                .setPositiveButton(getString(R.string.ok), (dialog1, which) -> {})
                .show();
    }

    @Override
    public void showMockedLocationProhibited(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.mocked_location_detected)
            .setPositiveButton(getString(R.string.ok), (dialog1, which) ->{})
            .show();
    }

    private void moveCameraSmoothly(Location location) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
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

    private void showNoGeoPermissionSnackbar() {
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

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }
}