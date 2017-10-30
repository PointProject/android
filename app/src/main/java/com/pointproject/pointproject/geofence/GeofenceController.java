package com.pointproject.pointproject.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.pointproject.pointproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeofenceController implements OnCompleteListener<Void> {

    private static final String TAG = GeofenceController.class.getSimpleName();

    private Context context;

    private GeofencingClient mGeofencingClient;

    private PendingIntent mGeofencePendingIntent;

    /**
     * Initialize GeofencingClient with context*/
    public GeofenceController(Context context) {
        this.context = context;

        // Initially set the PendingIntent to null
        mGeofencePendingIntent = null;

        mGeofencingClient = LocationServices.getGeofencingClient(context);

        Log.d(TAG, "Initialized GeofenceController");
    }

    /**
     * Adds all geofences to Geofence client and start listenting for updates
     *
     * @param geofences map with key as geofence id and value as geofence coordinates
     */
    public void addGeofences(Map<String, LatLng> geofences) {
        ArrayList<Geofence> mGeofenceList = new ArrayList<>();

        if(geofences.isEmpty())
            return;

        for (Map.Entry<String, LatLng> entry : geofences.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    //Set geofence id
                    .setRequestId(entry.getKey())

                    // Setting geofence region
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            context.getResources().getInteger(R.integer.geofences_radius_in_meters)
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }

        GeofencingRequest.Builder requestBuilder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence
        requestBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service
        requestBuilder.addGeofences(mGeofenceList);
        //Add Pending Intent to geofences, which will be triggered on events. Set on Listener,
//        which will catch results on adding and removing geofences
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGeofencingClient.addGeofences(requestBuilder.build(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    /**
     * Removes all available geofences
     */
    public void removeAllGeofences() {
        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }

    /**
     * Removes geofences by ids
     *
     * @param geofencesRequestIds List with geofence ids, which should be removed
     */
    public void removeGeofences(List<String> geofencesRequestIds){
        mGeofencingClient.removeGeofences(geofencesRequestIds);
    }

    /**
     * Runs when the result of calling {@link #addGeofences(Map)} and/or
     * {@link #removeGeofences(List)}, {@link #removeAllGeofences() is available.
     *
     * @param task the resulting Task, containing either a result or error.
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "Geofences were successfully processed");
        } else {
            Log.w(TAG, task.getException());
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceIntentService.class);
        // Use FLAG_UPDATE_CURRENT so that we get the same pending intent back when creating and
        //removing geofences
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
