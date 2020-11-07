package com.example.usertrackingsystem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class adminhome extends FragmentActivity implements OnMapReadyCallback {
    EditText et1, et2;
    Button bt;
    TextView tv;
    LatLng lng;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        bt = findViewById(R.id.bt1);
        tv = findViewById(R.id.tv1);
        SharedPreferences sp1 = getSharedPreferences("mysp1", MODE_PRIVATE);
        final String aname = sp1.getString("adminname", "NA");
        tv.setText("Welcome " + aname);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(aname + "_admin")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    final String email = et1.getText().toString();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getData().get("email").toString().equals(email)) {
                                            final String user = document.getData().get("name").toString();
                                            db.collection(user + "_user")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                String date = et2.getText().toString();

                                                                mMap.clear();
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if (document.getData().get("date").toString().equals(date)) {
                                                                        String time = document.getData().get("time").toString();

                                                                        float lat = Float.parseFloat(document.getData().get("lat").toString());
                                                                        float longi = Float.parseFloat(document.getData().get("long").toString());
                                                                        lng = new LatLng(lat, longi);
                                                                        CameraUpdate cu=CameraUpdateFactory.newLatLngZoom(lng,15);
                                                                        mMap.animateCamera(cu);
                                                                        MarkerOptions mo=new MarkerOptions();
                                                                        mo.position(lng);
                                                                        try{
                                                                            Geocoder gc=new Geocoder(adminhome.this);
                                                                            List<Address> addresses=gc.getFromLocation(lng.latitude,lng.longitude,1);
                                                                            Address ad=addresses.get(0);
                                                                            mo.title(ad.getAddressLine(1)+","+" at time "+time);
                                                                            mMap.addMarker(mo);
                                                                        }
                                                                        catch (Exception e)
                                                                        {
                                                                            Toast.makeText(adminhome.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                }
                                                            } else {
                                                                Toast.makeText(adminhome.this, "error in user", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }

                                    }
                                } else {
                                    Toast.makeText(adminhome.this, "Error in admin", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
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
        LatLng bareilly = new LatLng(28.3670, 79.4304);
        mMap.addMarker(new MarkerOptions().position(bareilly).title("Marker in Bareilly"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bareilly));
    }
}
