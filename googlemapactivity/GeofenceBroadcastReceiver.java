package com.example.googlemapactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        String geofenceId = intent.getStringExtra("geofenceId");
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
  //      Toast.makeText(context,"Geofence triggered ...",Toast.LENGTH_SHORT).show();
        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList) {
            Log.d(TAG, "onRecieve:" + geofence.getRequestId());
        }
 //       Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "Entering into Traffic Zone", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("Entering into Traffic Zone", "", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "IN the Traffic Zone", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("IN the Traffic Zone", "", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "Exiting from The traffic Zone", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("Exiting from The traffic Zone", "", MapsActivity.class);
                break;
        }

    }
}
