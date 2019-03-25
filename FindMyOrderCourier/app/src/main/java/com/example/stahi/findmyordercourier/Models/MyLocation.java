package com.example.stahi.findmyordercourier.Models;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

public class MyLocation implements Serializable {

    @SerializedName("id")
    public int Id;

    @SerializedName("latitude")
    public double Latitude;

    @SerializedName("longitude")
    public double Longitude;

    @SerializedName("speed")
    public double Speed;

    @SerializedName("courierId")
    public int CourierId;

    @SerializedName("locationTime")
    public Date Time;

    public MyLocation(double Latitude, double Longitude, int UserId){
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.CourierId = UserId;
        this.Time = new Date();
    }

}
