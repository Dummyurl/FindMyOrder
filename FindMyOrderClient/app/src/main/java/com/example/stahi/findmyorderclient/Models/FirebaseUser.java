package com.example.stahi.findmyorderclient.Models;

/**
 * Created by stahi on 4/15/2018.
 */

public class FirebaseUser {
    public String uid;
    public String email;
    public String firebaseToken;

    public FirebaseUser(){

    }

    public FirebaseUser(String uid, String email, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }
}