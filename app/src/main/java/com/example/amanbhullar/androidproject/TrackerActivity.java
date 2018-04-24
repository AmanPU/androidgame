package com.example.amanbhullar.androidproject;

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
import com.google.firebase.database.ValueEventListener;

public class TrackerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FirebaseDatabase db;

    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseDatabase.getInstance();
        root = db.getReference();

//        readData();
        subscribeToUpdates();

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
//        43.760386, -79.237016
//        LatLng swethaHome = new LatLng(43.760386, -79.237016);
//        mMap.addMarker(new MarkerOptions().position(swethaHome).title("Swetha"));
//
//        //43.761098, -79.237789
//        LatLng static1 = new LatLng(43.761098, -79.237789);
//        mMap.addMarker(new MarkerOptions().position(static1).title("static1"));
//
//        //43.761292, -79.235852
//        LatLng static2 = new LatLng(43.761292, -79.235852);
//        mMap.addMarker(new MarkerOptions().position(static2).title("static2"));
//
//        ////43.762914, -79.236899
//        LatLng static3 = new LatLng(43.762914, -79.236899);
//        mMap.addMarker(new MarkerOptions().position(static3).title("static3"));
//
//        //43.760794, -79.234999
//        LatLng static4 = new LatLng(43.760794, -79.234999);
//        mMap.addMarker(new MarkerOptions().position(static4).title("static4"));
//
//        //43.759405, -79.236286
//        LatLng static5 = new LatLng(43.759405, -79.236286);
//        mMap.addMarker(new MarkerOptions().position(static5).title("static5"));




//        mMap.moveCamera(CameraUpdateFactory.newLatLng(swethaHome));



//        mMap.setMinZoomPreference(14.0f);
//        mMap.setMaxZoomPreference(40.0f);
//        mMap.zoo
    }


    private void subscribeToUpdates() {



        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

//                MapCoordinates mapCoordinates = dataSnapshot.getC(MapCoordinates.class);

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    FBData mapCoordinates = childSnapshot.getValue(FBData.class);
                    LatLng static2 = new LatLng(mapCoordinates.latitude, mapCoordinates.longtitude);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(static2).title("static2"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(static2));

                    mMap.setMinZoomPreference(14.0f);
                    mMap.setMaxZoomPreference(40.0f);

                    Log.d ("DATA", mapCoordinates.toString());

                }

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
                Log.d("DB", "Failed to read value.", error.toException());
            }
        });
    }

    private void readData() {
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATA", dataSnapshot.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
