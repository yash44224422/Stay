package com.example.houserentalmanagement.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentalmanagement.MemberModel;
import com.example.houserentalmanagement.R;
import com.example.houserentalmanagement.adapter.SeeMemberAdapterOwner;
import com.example.houserentalmanagement.houseOwner.RegisterOwner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMembers extends AppCompatActivity {

    String houseId;
    String userId;
    private RecyclerView rv_showAllFood;
    private SeeMemberAdapterOwner adapter;
    private ArrayList<MemberModel> mList = new ArrayList<>();
    private TextView tv_noMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members2);

        Intent intent = getIntent();
        houseId = intent.getStringExtra("houseId");
        String noOfRoom = intent.getStringExtra("noOfRoom");
        String rentPerRoom = intent.getStringExtra("rentPerRoom");
        String houseDescription = intent.getStringExtra("houseDescription");
        String houseLocation = intent.getStringExtra("houseLocation");
        String houseImage = intent.getStringExtra("houseImage");
        userId = intent.getStringExtra("userId");

        rv_showAllFood = findViewById(R.id.recyclerView);
        tv_noMembers = findViewById(R.id.tv_noMembers); // Ensure you have this TextView in your XML layout
        rv_showAllFood.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewMembers.this);
        rv_showAllFood.setLayoutManager(linearLayoutManager);
        getAllArticle();
    }

    private void getAllArticle() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(RegisterOwner.MEMBERS).child(userId).child(houseId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MemberModel member = dataSnapshot.getValue(MemberModel.class);
                            mList.add(member);
                        }
                        if (mList.isEmpty()) {
                            showNoMembersMessage();
                        } else {
                            adapter = new SeeMemberAdapterOwner(ViewMembers.this, mList);
                            rv_showAllFood.setAdapter(adapter);
                        }
                    } else {
                        showNoMembersMessage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ViewMembers", "DatabaseError: " + error.getMessage());
                }
            });
        }
    }

    private void showNoMembersMessage() {
        tv_noMembers.setVisibility(View.VISIBLE);
        rv_showAllFood.setVisibility(View.GONE);
    }
}
