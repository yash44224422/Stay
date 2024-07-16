package com.example.houserentalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.houserentalmanagement.houseOwner.RegisterOwner;
import com.example.houserentalmanagement.user.RegisterUser;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btn_houseOwner, btn_user;
    FirebaseAuth auth =FirebaseAuth.getInstance();
    @Override
    protected void onStart() {
        super.onStart();
            if(auth.getCurrentUser() != null){
//                startActivity(new Intent(MainActivity.this, AddHouse.class));
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_houseOwner = findViewById(R.id.btn_houseOwner);
        btn_user = findViewById(R.id.btn_user);

        btn_houseOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterOwner.class));
            }
        });

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterUser.class));
            }
        });

    }
}