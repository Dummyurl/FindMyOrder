package com.example.stahi.findmyorderclient.Models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contact  implements Serializable {

    //Ce vine din API, SerializedName = fix numele din api

    public Contact() {
    }

    @SerializedName("message")
    public String Message;

    @SerializedName("userId")
    public int UserId;

}