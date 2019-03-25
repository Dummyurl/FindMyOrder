package com.example.stahi.findmyorderclient.Models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by stahi on 4/15/2018.
 */

public class UserRelationship implements Serializable {

    @SerializedName("clientId")
    public int FirstUserId;

    @SerializedName("client")
    @Nullable
    public User FirstUser ;

    @SerializedName("courierId")
    public int SecondUserId ;

    @SerializedName("courier")
    @Nullable
    public User SecondUser ;


}
