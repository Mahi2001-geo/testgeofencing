package com.example.googlemapactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Application;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;



public class MapsActivity
//       extends AppCompatActivity  implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener{
        extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;

    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private float GEOFENCE_RADIUS = 200;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";
//    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng anakapalle = new LatLng(17.6896,83.0024);
//        LatLng latlng = new LatLng(17.6896,83.0024);
        List<LatLng> a=new ArrayList<>();
        a.add(new LatLng(17.73500000,83.31972222));
        a.add(new LatLng(17.73805556,83.32333333));
        a.add(new LatLng(17.74277778,83.32777778));
        a.add(new LatLng(17.74750000,83.33111111));
        a.add(new LatLng(17.75527778,83.33250000));
//        mMap.addMarker(new MarkerOptions().position(anakapalle).title("Marker in Anakapalle"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(anakapalle));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(anakapalle,16f));
        enableUserLocation();
//        mMap.setOnMapLongClickListener(this::onMapLongClick);
//        mMap.setOnMapLongClickListener(this);
        for(LatLng i:a) {
            addMarker(i);
            addCircle(i, GEOFENCE_RADIUS);
            addGeofence(i, GEOFENCE_RADIUS);
        }
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                mMap.setMyLocationEnabled(true);
            } else {
                //We do not have the permission..
            }
        }
    }

    @Override
    public void onMapLongClick( LatLng latLng) {
     //   mMap.clear();
        addMarker(latLng);
        addCircle(latLng,GEOFENCE_RADIUS);
        addGeofence(latLng,GEOFENCE_RADIUS);

        }
    private void addGeofence(LatLng latLng, float radius) {
        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }
    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0,0));
        circleOptions.fillColor(Color.argb(64, 255, 0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

}
