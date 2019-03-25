package com.example.stahi.findmyordercourier.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetUserRelationshipResponse  implements Serializable {

    @SerializedName("UserRelationship")
    public ArrayList<UserRelationship> UserRelationship;

    public GetUserRelationshipResponse(){}

}
