package com.example.stahi.findmyorderclient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stahi on 4/15/2018.
 */

public class GetUserResponse implements Serializable {


    public GetUserResponse(){}

    //Lista primita din API: Users

    @SerializedName("Users")
    public ArrayList<User> Users;


}