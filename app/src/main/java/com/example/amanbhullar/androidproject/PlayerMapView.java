package com.example.amanbhullar.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class PlayerMapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FirebaseDatabase db;

    DatabaseReference root;

    LocationManager locationManager;
    Location location;
    public static final int RequestPermissionCode = 1;
    Boolean GpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = FirebaseDatabase.getInstance();
        root = db.getReference();

        Log.d("HERE","HERE");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.shouldShowRequestPermissionRationale(PlayerMapView.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(PlayerMapView.this, "ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(PlayerMapView.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }


        subscribeToUpdates();

    }

    private void subscribeToUpdates() {



        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                Toast.makeText(getApplicationContext(), "Broadcasted message.",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("DB", "Failed to read value.", error.toException());
            }
        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                Log.d ("HERE","HERE");

//                locationManager.re

//                Location startingLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//                Log.d ("Location", startingLocation.getLatitude() +" "+startingLocation.getLongitude());
//                MarkerOptions mp = new MarkerOptions();
//
//                mp.position(new LatLng(startingLocation.getLatitude(), startingLocation.getLongitude()));
//
//                mp.title("my position");
//
//                mMap.addMarker(mp);
//
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        new LatLng(startingLocation.getLatitude(), startingLocation.getLongitude()), 100));
//
//                FBData fbData = new FBData(startingLocation.getLatitude(), startingLocation.getLongitude());
//
//                root.child("messages").child("Player").setValue(fbData);


                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("TAG", "LOCATION SERVICES");

                        mMap.clear();

                        MarkerOptions mp = new MarkerOptions();

                        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                        mp.title("my position");

                        mMap.addMarker(mp);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()), 100));

                        FBData fbData = new FBData(location.getLatitude(), location.getLongitude());

                        root.child("messages").child("Device").setValue(fbData);

                        Log.d ("DIST", getDist(location.getLatitude(), location.getLongitude()) + "");

                        double distanceFromFlag = getDist(location.getLatitude(),location.getLongitude());

                        if ( distanceFromFlag < 10f){
                            Toast.makeText(getApplicationContext(), "Flag Found",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), distanceFromFlag+"",
                                    Toast.LENGTH_SHORT).show();
                        }


                        if (distanceFromFlag > 50f){
                            Toast.makeText(getApplicationContext(), "You are out of play field",
                                    Toast.LENGTH_SHORT).show();
                        }



                        Log.d("Here", "LocationChanged");
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });

                break;
        }
    }





    public double getDist(double latitude, double longitude) {

        int Radius = 6371000;// radius of earth in Km
//        43.760504, -79.237915
//        43.760464, -79.237507
//        43.774254, -79.335607
        double lat = 43.774254;
        double log = -79.335607;
        double dLat = Math.toRadians(lat - latitude);
        double dLon = Math.toRadians(log - longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude))
                * Math.cos(Math.toRadians(lat)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin( Math.sqrt(a) );
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        double rad = Radius * c;
        Log.d("dist", String.valueOf(rad));
        return rad;
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
