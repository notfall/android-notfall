package com.mayassin.android.notfall.plainjava;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anurag on 1/29/17.
 */

public class Responder extends User {

    public Responder(){
    }

    public Responder(String username, int age, String fullName, String location) {
        super(username, age, fullName, location);
    }
}