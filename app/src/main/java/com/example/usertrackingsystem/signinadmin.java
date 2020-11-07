package com.example.usertrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class signinadmin extends AppCompatActivity {
    EditText et1, et2;
    Button bt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinadmin);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        bt = findViewById(R.id.bt1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp1=getSharedPreferences("mysp1",MODE_PRIVATE);
                final SharedPreferences.Editor ed1=sp1.edit();
                db.collection("Admins")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String email=et1.getText().toString();
                                String pass=et2.getText().toString();

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.getData().get("email").toString().equals(email))
                                        {
                                            if(document.getData().get("password").toString().equals(pass))
                                            {
                                                Toast.makeText(signinadmin.this, "SIGNIN successful", Toast.LENGTH_SHORT).show();
                                                ed1.putString("adminname",document.getData().get("name").toString());
                                                ed1.commit();
                                                Intent in=new Intent(signinadmin.this,adminhome.class);
                                                startActivity(in);
                                            }
                                            else {
                                                Toast.makeText(signinadmin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                } else {
                                    Toast.makeText(signinadmin.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
