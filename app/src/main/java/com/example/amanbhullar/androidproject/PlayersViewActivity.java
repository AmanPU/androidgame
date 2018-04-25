package com.example.amanbhullar.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PlayersViewActivity extends AppCompatActivity implements LocationListener {



    FusedLocationProviderClient locationProvider;

    FirebaseDatabase db;

    DatabaseReference root;
    private GoogleMap mMap;

    LocationManager locationManager;
    Location location;
    public static final int RequestPermissionCode = 1;
    Boolean GpsStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_view);

        db = FirebaseDatabase.getInstance();
        root = db.getReference();




        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.shouldShowRequestPermissionRationale(PlayersViewActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {


            Toast.makeText(PlayersViewActivity.this, "ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(PlayersViewActivity.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }

        locationProvider = LocationServices.getFusedLocationProviderClient(this);

        subscribeToUpdatesInDatabase();

    }

    private void checkLocationandAddToMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);
            return;
        }

        locationProvider.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {



                            // Logic to handle location object
                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are Here").alpha(3);
                            Marker currentLocationMarker = mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()) , 17.0f));//9

                        }
                    }
                });
    }

    private void subscribeToUpdatesInDatabase() {



        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Toast.makeText(getApplicationContext(), "Broadcasted message.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    public void CheckGpsStatus() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void EnableRuntimePermission() {

//
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:


                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


                FBData fbData = new FBData(lastLocation.getLatitude(), lastLocation.getLongitude());


                root.child("messages").push().setValue(fbData);

                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
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
}