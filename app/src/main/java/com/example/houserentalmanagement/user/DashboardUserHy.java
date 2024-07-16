package com.example.houserentalmanagement.user;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentalmanagement.HouseModel;
import com.example.houserentalmanagement.R;
import com.example.houserentalmanagement.adapter.SeeHouseAdapterUser;
import com.example.houserentalmanagement.houseOwner.RegisterOwner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardUserHy extends AppCompatActivity {

    private RecyclerView rv_showAllFood;
    private SeeHouseAdapterUser adapter;
    private ArrayList<HouseModel> mList = new ArrayList<>();
    private static final String TAG = "DashboardUserHy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardhy_user);

        rv_showAllFood = findViewById(R.id.recyclerView);
        rv_showAllFood.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardUserHy.this);
        rv_showAllFood.setLayoutManager(linearLayoutManager);

        // Get the selected city from the intent
        String selectedCity = getIntent().getStringExtra("city");
        getAllArticle(selectedCity);
    }

    private void getAllArticle(String city) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(RegisterOwner.HOUSES);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot houseSnapshot : userSnapshot.getChildren()) {
                            HouseModel house = houseSnapshot.getValue(HouseModel.class);
                            if (house != null && city.equals(house.getHouseLocation())) {
                                mList.add(house);
                            }
                        }
                    }
                    adapter = new SeeHouseAdapterUser(DashboardUserHy.this, mList);
                    rv_showAllFood.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.getMessage());
                }
            });
        }
    }
}
