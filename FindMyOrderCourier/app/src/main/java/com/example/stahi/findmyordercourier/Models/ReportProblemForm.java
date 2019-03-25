package com.example.stahi.findmyordercourier.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportProblemForm  implements Serializable {

    public ReportProblemForm() {
    }

    @SerializedName("message")
    public String Message;

    @SerializedName("userId")
    public int UserId;

}