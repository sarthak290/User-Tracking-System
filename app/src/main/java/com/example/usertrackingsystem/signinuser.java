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

public class signinuser extends AppCompatActivity {
    EditText et1, et2, et3;
    Button bt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinuser);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        bt = findViewById(R.id.bt1);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String admin = et2.getText().toString();
                SharedPreferences sp=getSharedPreferences("mysp",MODE_PRIVATE);
                final SharedPreferences.Editor ed=sp.edit();
                db.collection(admin + "_admin")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String password = et3.getText().toString();
                                String email = et1.getText().toString();
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getData().get("email").toString().equals(email)) {
                                            if (document.getData().get("password").toString().equals(password)) {
                                                Toast.makeText(signinuser.this, "SignIn Successful", Toast.LENGTH_SHORT).show();
                                                ed.putString("username",document.getData().get("name").toString());
                                                ed.commit();
                                                Intent in=new Intent(signinuser.this,userhome.class);
                                                startActivity(in);
                                            } else {
                                                Toast.makeText(signinuser.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(signinuser.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

}
