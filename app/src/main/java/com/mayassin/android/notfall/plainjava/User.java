package com.mayassin.android.notfall.plainjava;

/**
 * Created by Anurag on 1/29/17.
 */

public class User {
    private String username;
    private int age;
    private String fullName;
    private double latitude;
    private double longitude;
    private int type;
    String certificateTitle;
    String insuranceProvider;
    String primaryCaretaker;

    public User(){
    }

    public String getType() {
        switch (type) {
            default:
            case 0:
                return "Patient";
            case 1:
                return "Caretaker";
            case 2:
                return "First Responder";
        }
    }

    public User(int type,String username, int age, String fullName, String location){

        this.username = username;
        this.age = age;
        this.fullName = fullName;
        String[] splitLocations = location.split("\\s*,\\s*");
        this.latitude = Double.parseDouble(splitLocations[0]);
        this.longitude = Double.parseDouble(splitLocations[1]);
    }

    public User(String fullName, int type) {
        this.fullName = fullName;
        this.type = type;
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
