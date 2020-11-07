package com.example.usertrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class userhome extends AppCompatActivity implements LocationListener {
    TextView tv1,tv2;
    Button bt;
    LatLng lng;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        bt = findViewById(R.id.bt1);
        SharedPreferences sp = getSharedPreferences("mysp", MODE_PRIVATE);
        String uname = sp.getString("username", "NA");
        tv1.setText("Welcome " + uname);
        ActivityCompat.requestPermissions(userhome.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    Toast.makeText(userhome.this, "Please wait..", Toast.LENGTH_SHORT).show();

                    return;
                }

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 10, userhome.this);
                tv2.setText("Your location is been tracked");
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        lng = new LatLng(location.getLatitude(), location.getLongitude());
        Toast.makeText(userhome.this, ""+lng.latitude+","+lng.longitude+"is updated", Toast.LENGTH_SHORT).show();
        String currentDate=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime=new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date());
        SharedPreferences sp = getSharedPreferences("mysp", MODE_PRIVATE);
        String uname = sp.getString("username", "NA");
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("lat", lng.latitude);
        user.put("long", lng.longitude);
        user.put("date",currentDate);
        user.put("time",currentTime);
// Add a new document with a generated ID
        db.collection(uname+"_user")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(userhome.this, ""+lng.latitude+","+lng.longitude+"is updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(userhome.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
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
