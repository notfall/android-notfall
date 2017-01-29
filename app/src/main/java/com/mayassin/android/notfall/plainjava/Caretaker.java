package com.mayassin.android.notfall.plainjava;

/**
 * Created by Anurag on 1/28/17.
 */

public class Caretaker extends User {

    String certificateTitle;


    public Caretaker() {
    }

    public Caretaker(String username, int age, String certificateTitle, String fullName, String location) {
        super(username, age, fullName, location);
        this.certificateTitle = certificateTitle;
    }


    public String getCertificateTitle() {
        return certificateTitle;
    }

    public void setCertificateTitle(String certificateTitle) {
        this.certificateTitle = certificateTitle;
    }
}
