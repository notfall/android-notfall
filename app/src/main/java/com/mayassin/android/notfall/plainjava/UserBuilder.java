package com.mayassin.android.notfall.plainjava;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anurag on 1/29/17.
 */

public class UserBuilder {
    private DatabaseReference mFirebaseDatabaseReference;
    protected Patient patient;
    protected Caretaker caretaker;
    protected Responder responder;
    String username;

    public UserBuilder(String username) {
        patient = new Patient();
        caretaker = new Caretaker();
        responder = new Responder();
    }

    public Patient createPatient(){
        return patient;
    }

    public Caretaker createCaretaker(){
        return caretaker;
    }

    public Responder createResponder(){
        return responder;
    }

    public void getFromDatabase(String userId, int type){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference queryref = mFirebaseDatabaseReference.child(userId).child(""+ type);
        queryref.addListenerForSingleValueEvent(new ValueEventListener() {

            // check if Patient, Caretaker, or Responder
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == 0){
                    createPatient();
                }
                else if(dataSnapshot.getValue() == 1){
                    createCaretaker();
                }
                else{
                    createResponder();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // wah wah wah waaahhhhhh ;)
            }
        });
    }
}
