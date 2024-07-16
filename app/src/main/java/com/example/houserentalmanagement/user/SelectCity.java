package com.example.houserentalmanagement.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.houserentalmanagement.R;

public class SelectCity extends AppCompatActivity {

    CardView cv_mumbai;
    CardView cv_hyderabad;
    CardView cv_chennai;
    CardView cv_bangalore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        cv_mumbai = findViewById(R.id.cv_img_mumbai);
        cv_hyderabad = findViewById(R.id.cv_img_hyderabad);
        cv_chennai = findViewById(R.id.cv_img_chennai);
        cv_bangalore = findViewById(R.id.cv_img_banglore);

        cv_mumbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dir = new Intent(getApplicationContext(), DashboardUserHy.class);
                dir.putExtra("city", "Mumbai");
                startActivity(dir);
            }
        });

        cv_hyderabad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dir = new Intent(getApplicationContext(), DashboardUserHy.class);
                dir.putExtra("city", "Hyderabad");
                startActivity(dir);
            }
        });

        cv_chennai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dir = new Intent(getApplicationContext(), DashboardUserHy.class);
                dir.putExtra("city", "Chennai");
                startActivity(dir);
            }
        });

        cv_bangalore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dir = new Intent(getApplicationContext(), DashboardUserHy.class);
                dir.putExtra("city", "Bangalore");
                startActivity(dir);
            }
        });
    }
}
