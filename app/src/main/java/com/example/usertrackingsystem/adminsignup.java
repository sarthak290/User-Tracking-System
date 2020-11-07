package com.example.usertrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class adminsignup extends AppCompatActivity {
EditText et1,et2,et3;
Button bt;
TextView tv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsignup);
        et1=findViewById(R.id.et1);
        et2=findViewById(R.id.et2);
        et3=findViewById(R.id.et3);
        bt=findViewById(R.id.bt1);
        tv=findViewById(R.id.bt2);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et1.getText().toString()=="" ||et2.getText().toString()=="" ||et3.getText().toString()=="" )
                {
                    Toast.makeText(adminsignup.this, "Fields Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", et1.getText().toString());
                    user.put("email", et2.getText().toString());
                    user.put("password", et3.getText().toString());

// Add a new document with a generated ID
                    db.collection("Admins")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(adminsignup.this, "SignUP Successful", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(adminsignup.this, signinadmin.class);
                                    startActivity(in);
                                    adminsignup.this.finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(adminsignup.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(adminsignup.this,signinadmin.class);
                startActivity(in);
            }
        });
    }
}
