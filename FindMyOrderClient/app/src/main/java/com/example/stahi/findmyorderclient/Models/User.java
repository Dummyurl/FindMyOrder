package com.example.stahi.findmyorderclient.Models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stahi on 4/15/2018.
 */

public class User implements Serializable {

    //Ce vine din API, SerializedName = fix numele din api

    public User() {
    }

    @SerializedName("id")
    public int Id;

    @SerializedName("email")
    public String Email;

    @SerializedName("password")
    public String Password;

    @SerializedName("oldPassword")
    public String OldPassword;

    @SerializedName("token")
    public String Token;

    @SerializedName("firebaseToken")
    public String FirebaseToken;

    @SerializedName("isCurier")
    public Boolean IsCurier;


    @SerializedName("userDetails")
    public UserDetails UserDetails;

    @SerializedName("lastLatitude")
    public double LastLatitude;

    @SerializedName("lastLongitude")
    public double LastLongitude;

    @SerializedName("clientPackagesName")
    @Nullable
    public List<String> clientPackagesName ;

}


