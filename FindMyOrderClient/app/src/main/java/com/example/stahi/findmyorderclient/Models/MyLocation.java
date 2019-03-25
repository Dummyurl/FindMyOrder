package com.example.stahi.findmyorderclient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by stahi on 4/15/2018.
 */

public class MyLocation implements Serializable {

    @SerializedName("id")
    public int Id;

    @SerializedName("latitude")
    public double Latitude;

    @SerializedName("longitude")
    public double Longitude;

    @SerializedName("userId")
    public int UserId;

    public MyLocation(double Latitude, double Longitude, int UserId){
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.UserId = UserId;
    }

}
