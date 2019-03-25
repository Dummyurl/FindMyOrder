package com.example.stahi.findmyordercourier.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class FinalizeCommand implements Serializable {

    @SerializedName("courierId")
    public int CourierId;

    @SerializedName("clientId")
    public int ClientId;

    @SerializedName("date")
    public Date Date;

    @SerializedName("observations")
    public String Observations;


    public FinalizeCommand(int courierId, int clientId, String observations) {
        this.CourierId = courierId;
        this.ClientId = clientId;
        this.Observations = observations;
        this.Date = new Date();
    }
}