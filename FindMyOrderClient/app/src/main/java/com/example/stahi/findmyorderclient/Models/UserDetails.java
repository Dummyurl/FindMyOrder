package com.example.stahi.findmyorderclient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by stahi on 4/15/2018.
 */

public class UserDetails implements Serializable {

    public UserDetails(){}

    @SerializedName("id")
    public int Id;

    @SerializedName("name")
    public String Name;

    @SerializedName("address")
    public String Address;

    @SerializedName("phoneNr")
    public String PhoneNr;

    @SerializedName("userId")
    public int UserId;

}