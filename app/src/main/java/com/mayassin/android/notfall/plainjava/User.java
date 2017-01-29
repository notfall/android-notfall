package com.mayassin.android.notfall.plainjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Anurag on 1/29/17.
 */

public class User {
    public String username;
    public Long age;
    public String fullName;
    public double latitude;
    public double longitude;
    public String phoneNumber;
    public int type;
    private StorageReference storageRef;

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public Bitmap profileImage;
    String certificateTitle;
    String insuranceProvider;
    String primaryCaretaker;


    public String getType() {
        switch (type) {
            default:
            case 0:
                return "Patient";
            case 1:
                return "Caretaker";
            case 2:
                return "First Responder";
            case 3:
                return "Hospital";
        }
    }

    public String getLocation() {
        return latitude +"," + longitude;
    }

    public User(String fullName, int type, String username) {
        this.fullName = fullName;
        this.type = type;
        this.username = username;


    }

    public User(String username) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://notfall-aac12.appspot.com");
        this.username = username;
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference queryref = mFirebaseDatabaseReference.child("users").child(username);
        queryref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    if(child.getKey().equals("full_name")) {
                        fullName = child.getValue().toString();
                    }
                    if(child.getKey().equals("type")) {
                        type = (int) (long) child.getValue();
                    }
                    if(child.getKey().equals("age")) {
                        age = (Long) child.getValue();
                    }
                    if(child.getKey().equals("location")) {
                        String location[] = child.getValue().toString().split(",");
                        latitude = Double.parseDouble(location[0]);
                        longitude = Double.parseDouble(location[1]);
                    }
                    if(child.getKey().equals("phone_number")) {
                        phoneNumber = child.getValue().toString();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        StorageReference pathReference = storageRef.child("users").child(username).child("profilepic.jpg");
        pathReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                profileImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // WHEN IMAGE FAILED
            }
        });
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
