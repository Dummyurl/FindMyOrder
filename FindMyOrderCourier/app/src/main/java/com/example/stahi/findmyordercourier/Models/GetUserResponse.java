package com.example.stahi.findmyordercourier.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetUserResponse implements Serializable {


    public GetUserResponse(){}

    //Lista primita din API: Users

    @SerializedName("Users")
    public ArrayList<User> Users;


}