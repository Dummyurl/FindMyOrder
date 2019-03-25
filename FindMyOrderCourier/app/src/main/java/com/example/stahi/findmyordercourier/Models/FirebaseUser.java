package com.example.stahi.findmyordercourier.Models;

public class FirebaseUser{
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