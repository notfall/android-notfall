package com.mayassin.android.notfall.plainjava;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Patient extends User {

  String insuranceProvider;
  String primaryCaretaker;

  public Patient() {
  }

  public Patient(String username, int age, String fullName, String insuranceProvider, String location, String primaryCaretaker) {

    super(username, age, fullName, location);

    this.insuranceProvider = insuranceProvider;
    this.primaryCaretaker = primaryCaretaker;
  }

  public String getInsuranceProvider() {
    return insuranceProvider;
  }

  public void setInsuranceProvider(String insuranceProvider) {
    this.insuranceProvider = insuranceProvider;
  }

  public String getPrimaryCaretaker() {
    return primaryCaretaker;
  }

  public void setPrimaryCaretaker(String primaryCaretaker) {
    this.primaryCaretaker = primaryCaretaker;
  }
}
