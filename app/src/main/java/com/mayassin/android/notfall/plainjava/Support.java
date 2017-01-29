package com.mayassin.android.notfall.plainjava;

/**
 * Created by Anurag on 1/28/17.
 */

public class Support {
    int age;
    String certificateTitle;
    String fullName;
    double latitude;
    double longitude;
    int type;  // 0 is caretaker, 1 is responders

    public Support(int age, String certificateTitle, String fullName, String location, int type){
        this.age = age;
        this.certificateTitle = certificateTitle;
        this.fullName = fullName;
        String[] splitLocations = location.split("\\s*,\\s*");
        this.latitude = Double.parseDouble(splitLocations[0]);
        this.longitude = Double.parseDouble(splitLocations[1]);
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCertificateTitle() {
        return certificateTitle;
    }

    public void setCertificateTitle(String certificateTitle) {
        this.certificateTitle = certificateTitle;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
