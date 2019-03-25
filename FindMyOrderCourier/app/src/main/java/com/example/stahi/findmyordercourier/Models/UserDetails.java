package com.example.stahi.findmyordercourier.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
