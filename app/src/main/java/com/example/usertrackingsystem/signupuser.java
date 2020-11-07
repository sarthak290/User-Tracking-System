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

public class signupuser extends AppCompatActivity {
    EditText et1, et2, et3, et4;
    Button bt;
    TextView et5;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupuser);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        bt = findViewById(R.id.bt1);
        et5 = findViewById(R.id.et5);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = et1.getText().toString();
                final String email = et2.getText().toString();
                final String password = et3.getText().toString();
                final String admin = et4.getText().toString();
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("email", email);
                user.put("password", password);
                user.put("admin", admin);
// Add a new document with a generated ID
                db.collection(admin+"_admin")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(signupuser.this, "SignUp successful", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(signupuser.this, signinuser.class);
                                startActivity(in);
                                signupuser.this.finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(signupuser.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        et5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(signupuser.this, signinuser.class);
                startActivity(in);
                signupuser.this.finish();
            }
        });
    }
}
