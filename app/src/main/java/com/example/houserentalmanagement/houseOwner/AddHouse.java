package com.example.houserentalmanagement.houseOwner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.houserentalmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddHouse extends AppCompatActivity {

    ImageView iv_houseImage;
    EditText et_houseId, et_houseLocation, et_noOfRoom, et_rentPerRoom, et_houseDescription, et_phoneNo;
    Button btn_addHouse;

    private static final int STORAGE_PERMISSION_CODE = 100;
    StorageReference storageReference;
    public static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageString;
    private StorageTask uploadTask;

    private ProgressDialog progressDialog;

    // views for button
    private Button btnSelect, btnUpload;

    // view for image view

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        iv_houseImage = findViewById(R.id.iv_houseImage);
        et_houseId = findViewById(R.id.et_houseId);
        et_houseLocation = findViewById(R.id.et_houseLocation);
        et_noOfRoom = findViewById(R.id.et_noOfRoom);
        et_rentPerRoom = findViewById(R.id.et_rentPerRoom);
        et_houseDescription = findViewById(R.id.et_houseDescription);
        et_phoneNo = findViewById(R.id.et_phoneNo); // New field
        btn_addHouse = findViewById(R.id.btn_addHouse);

        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");

        iv_houseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();

            }
        });

        progressDialog = new ProgressDialog(AddHouse.this);

        btn_addHouse.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                uploadImage();


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createHouse(String houseId, String houseLocation, String noOfRoom, String rentPerRoom, String houseDescription, String phoneNo, String image) {
        progressDialog.dismiss();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(RegisterOwner.HOUSES).child(userId);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("houseId", houseId);
        hashMap.put("noOfRoom", noOfRoom);
        hashMap.put("houseDescription", houseDescription);
        hashMap.put("houseLocation", houseLocation);
        hashMap.put("rentPerRoom", rentPerRoom);
        hashMap.put("phoneNo", phoneNo); // New field
        hashMap.put("houseImage", image);
        hashMap.put("userId", userId);
        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(AddHouse.this, "Article Published Successfully", Toast.LENGTH_SHORT).show();
                    imageString = "";
                } else {
                    Toast.makeText(AddHouse.this, "Article Published Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddHouse.this, "Failed to create house", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(AddHouse.this, "House created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                iv_houseImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();

            }
        }
    }

        // UploadImage method
        private void uploadImage ()
        {
            if (filePath != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                // Defining the child of storageReference
                StorageReference ref
                        = storageReference
                        .child(
                                "images/"
                                        + UUID.randomUUID().toString() +  ".jpg");

                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)

                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot) {


                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // Image uploaded successfully
                                                // Dismiss dialog
                                                progressDialog.dismiss();
                                                progressDialog.setMessage("Publishing Your Article");
                                                progressDialog.setTitle("Adding...");
                                                progressDialog.setCanceledOnTouchOutside(false);
//                                                progressDialog.show();
                                                String houseId = et_houseId.getText().toString();
                                                String houseLocation = et_houseLocation.getText().toString();
                                                String noOfRoom = et_noOfRoom.getText().toString();
                                                String rentPerRoom = et_rentPerRoom.getText().toString();
                                                String houseDescription = et_houseDescription.getText().toString();
                                                String phoneNo = et_phoneNo.getText().toString(); // New field
                                                String image = uri.toString();
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    createHouse(houseId, houseLocation, noOfRoom, rentPerRoom, houseDescription, phoneNo, image);
                                                }
                                            }
                                        });

                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(AddHouse.this,
                                                "Failed " + e.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int) progress + "%");
                                    }
                                });
            }
        }


}
