package com.mayassin.android.notfall.plainjava;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Patient {

  String username;
  int age;
  String fullName;
  String insuranceProvider;
  double latitude;
  double longitude;
  String primaryCaretaker;

  public Patient() {
  }

  public Patient(String username, int age, String fullName, String insuranceProvider, String location, String primaryCaretaker) {

    this.username = username;
    this.age = age;
    this.fullName = fullName;
    this.insuranceProvider = insuranceProvider;
    String[] splitLocations = location.split("\\s*,\\s*");
    this.latitude = Double.parseDouble(splitLocations[0]);
    this.longitude = Double.parseDouble(splitLocations[1]);
    this.primaryCaretaker = primaryCaretaker;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getInsuranceProvider() {
    return insuranceProvider;
  }

  public void setInsuranceProvider(String insuranceProvider) {
    this.insuranceProvider = insuranceProvider;
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

  public String getPrimaryCaretaker() {
    return primaryCaretaker;
  }

  public void setPrimaryCaretaker(String primaryCaretaker) {
    this.primaryCaretaker = primaryCaretaker;
  }
}
